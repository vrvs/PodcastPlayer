package br.ufpe.cin.vrvs.podcastplayer.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.ufpe.cin.vrvs.podcastplayer.data.datasource.local.database.PodcastDatabase
import br.ufpe.cin.vrvs.podcastplayer.data.datasource.local.database.table.EpisodePersistedDownloaded
import br.ufpe.cin.vrvs.podcastplayer.data.datasource.local.database.table.fromEpisode
import br.ufpe.cin.vrvs.podcastplayer.data.datasource.local.database.table.fromPodcast
import br.ufpe.cin.vrvs.podcastplayer.data.datasource.local.database.table.toEpisode
import br.ufpe.cin.vrvs.podcastplayer.data.datasource.local.database.table.toPodcast
import br.ufpe.cin.vrvs.podcastplayer.data.datasource.remote.podcastindex.PodcastIndexApi
import br.ufpe.cin.vrvs.podcastplayer.data.datasource.remote.podcastindex.response.toEpisode
import br.ufpe.cin.vrvs.podcastplayer.data.datasource.remote.podcastindex.response.toPodcast
import br.ufpe.cin.vrvs.podcastplayer.data.model.Episode
import br.ufpe.cin.vrvs.podcastplayer.data.model.Podcast
import br.ufpe.cin.vrvs.podcastplayer.data.model.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.await
import java.io.File

class PodcastRepositoryImpl : PodcastRepository, KoinComponent {

    private val podcastDatabase: PodcastDatabase by inject()
    private val podcastIndexApi: PodcastIndexApi by inject()

    private val scope = CoroutineScope(Dispatchers.IO + NonCancellable)

    private var podcastsFeed: MutableLiveData<Result<List<Podcast>>>  = MutableLiveData()
    private var podcastsSubscribed: MutableLiveData<Result<List<Podcast>>> = MutableLiveData()
    private var podcast: MutableLiveData<Result<Podcast>> = MutableLiveData()
    private var subscribed: MutableLiveData<Result<Boolean>> = MutableLiveData()
    private var episode: MutableLiveData<Result<Episode>> = MutableLiveData()

    override fun getPodcastFeed(): LiveData<Result<List<Podcast>>> {
        val podcastsFeed: MutableLiveData<Result<List<Podcast>>>  = MutableLiveData()
        scope.launch {
            podcastsFeed.postValue(Result.Loading)
            try {
                val result = podcastIndexApi.getRecentFeed(30).await()
                podcastsFeed.postValue(Result.Success(result.feeds.map {
                    Podcast.toPodcast(it)
                }))
            } catch (e: Exception) {
                podcastsFeed.postValue(Result.Error(e))
            }
        }
        return podcastsFeed
    }

    override fun getSubscribedPodcast(): LiveData<Result<List<Podcast>>> {
        val podcastsSubscribed: MutableLiveData<Result<List<Podcast>>> = MutableLiveData()
        scope.launch {
            podcastsSubscribed.postValue(Result.Loading)
            try {
                val result = podcastDatabase.podcastDao().getPodcasts()
                podcastsSubscribed.postValue(Result.Success(result.filter {
                    it.subscribed
                }.map {
                    Podcast.toPodcast(it)
                }))
            } catch (e: Exception) {
                podcastsSubscribed.postValue(Result.Error(e))
            }
        }
        return podcastsSubscribed
    }

    override fun getPodcast(id: String): LiveData<Result<Podcast>> {
        val podcast: MutableLiveData<Result<Podcast>> = MutableLiveData()
        scope.launch {
            podcast.postValue(Result.Loading)
            try {
                podcast.postValue(Result.Success(getPodcastAwait(id)))
            } catch (e: Exception) {
                podcast.postValue(Result.Error(e))
            }
        }
        return podcast
    }

    override fun getEpisode(id: String): LiveData<Result<Episode>> {
        val episode: MutableLiveData<Result<Episode>> = MutableLiveData()
        scope.launch {
            episode.postValue(Result.Loading)
            try {
                val result = podcastDatabase.podcastDao().getEpisode(id)
                episode.postValue(Result.Success(Episode.toEpisode(result)))
            } catch (e: Exception) {
                episode.postValue(Result.Error(e))
            }
        }
        return episode
    }

    override fun updateDownloadedEpisode(episode: Episode, downloaded: Boolean, path: String): LiveData<Result<Boolean>> {
        val updated: MutableLiveData<Result<Boolean>> = MutableLiveData()
        scope.launch {
            updated.postValue(Result.Loading)
            try {
                val db = podcastDatabase.podcastDao()
                when {
                    db.hasEpisode(episode.id) -> {
                        db.updateDownloaded(EpisodePersistedDownloaded(episode.id,downloaded, path))
                    }
                    db.hasPodcast(episode.podcastId) -> {
                        getPodcastAwait(episode.podcastId)
                        db.updateDownloaded(EpisodePersistedDownloaded(episode.id,downloaded, path))
                    }
                    else -> {
                        subscribePodcastAwait(episode.podcastId)
                        db.updateDownloaded(EpisodePersistedDownloaded(episode.id,downloaded, path))
                    }
                }
                if (!downloaded) {
                    deleteFile(episode.path)
                }
            } catch (e: Exception) {
                updated.postValue(Result.Error(e))
            }
        }
        return updated
    }

    override fun subscribePodcast(id: String): LiveData<Result<Boolean>> {
        val subscribed: MutableLiveData<Result<Boolean>> = MutableLiveData()
        scope.launch {
            subscribed.postValue(Result.Loading)
            try {
                subscribed.postValue(Result.Success(subscribePodcastAwait(id)))
            } catch (e: Exception) {
                subscribed.postValue(Result.Error(e))
            }
        }
        return subscribed
    }

    override fun unsubscribePodcast(id: String): LiveData<Result<Boolean>> {
        val unsubscribed: MutableLiveData<Result<Boolean>> = MutableLiveData()
        scope.launch {
            unsubscribed.postValue(Result.Loading)
            try {
                unsubscribed.postValue(Result.Success(unsubscribePodcastAwait(id)))
            } catch (e: Exception) {
                unsubscribed.postValue(Result.Error(e))
            }
        }
        return unsubscribed
    }

    private suspend fun unsubscribePodcastAwait(id: String): Boolean {
        val db = podcastDatabase.podcastDao()
        db.clearPodcast(id)
        val episodes = db.getPodcastEpisodes(id)
        db.clearPodcastEpisodes(id)
        episodes.filter {
            it.path != ""
        }.forEach {
            deleteFile(it.path)
        }
        return true
    }

    private fun deleteFile(path: String) {
        val file = File(path).canonicalFile
        if (file.exists()) {
            file.delete()
        }
    }

    private suspend fun subscribePodcastAwait(id: String): Boolean {
        val podcast = getPodcastAwait(id)
        val db = podcastDatabase.podcastDao()
        if (!db.hasPodcast(id)) {
            podcast.subscribed = true
            db.insertPodcast(Podcast.fromPodcast(podcast))
            podcast.episodes.forEach {
                db.insertEpisode(Episode.fromEpisode(it))
            }
        }
        return true
    }

    private suspend fun getPodcastAwait(id: String): Podcast {
        val db = podcastDatabase.podcastDao()
        val answer: Podcast
        if (db.hasPodcast(id)) {
            val podcastResult = db.getPodcast(id)
            var episodeResult = db.getPodcastEpisodes(id)
            answer = Podcast.toPodcast(podcastResult)
            answer.episodes = episodeResult.map {
                Episode.toEpisode(it)
            }
            try {
                val episodeResultApi = podcastIndexApi.getEpisodes(id).await()
                val newEpisodes = episodeResultApi.episodes.filter { episodeRemote ->
                    answer.episodes.map { episodePersisted ->
                        episodePersisted.id
                    }.contains(episodeRemote.id.toString())
                }
                if (newEpisodes.isNotEmpty()) {
                    newEpisodes.map {
                        db.insertEpisode(Episode.fromEpisode(Episode.toEpisode(it)))
                    }
                }
                episodeResult = db.getPodcastEpisodes(id)
                answer.episodes = episodeResult.map {
                    Episode.toEpisode(it)
                }
            } catch (e: Exception) {
                // Do nothing - use local values
            }
        } else {
            val podcastResult = podcastIndexApi.getPodcast(id.toInt()).await()
            val episodeResult = podcastIndexApi.getEpisodes(id).await()
            answer = Podcast.toPodcast(podcastResult.feed)
            answer.episodes = episodeResult.episodes.map {
                Episode.toEpisode(it)
            }
        }
        return answer
    }
}