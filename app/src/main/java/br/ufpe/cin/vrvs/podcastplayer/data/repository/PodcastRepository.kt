package br.ufpe.cin.vrvs.podcastplayer.data.repository

import androidx.lifecycle.LiveData
import br.ufpe.cin.vrvs.podcastplayer.data.model.Episode
import br.ufpe.cin.vrvs.podcastplayer.data.model.Podcast
import br.ufpe.cin.vrvs.podcastplayer.data.model.Result

interface PodcastRepository {
    fun getPlayedPodcast() : LiveData<Result<Pair<String?, Long?>>>
    fun updatePlayedPodcast(id: String, time: Long)
    fun clearPlayedPodcast()
    fun getPodcastFeed() : LiveData<Result<List<Podcast>>>
    fun searchPodcast(query: String) : LiveData<Result<List<Podcast>>>
    fun getSubscribedPodcast() : LiveData<Result<List<Podcast>>>
    fun getPodcast(id: String) : LiveData<Result<Podcast>>
    fun getEpisode(id: String) : LiveData<Result<Episode>>
    fun updateDownloadedEpisode(episode: Episode, downloaded: Boolean, path: String = ""): LiveData<Result<Boolean>>
    fun subscribePodcast(id: String) : LiveData<Result<String>>
    fun unsubscribePodcast(id: String): LiveData<Result<String>>
}