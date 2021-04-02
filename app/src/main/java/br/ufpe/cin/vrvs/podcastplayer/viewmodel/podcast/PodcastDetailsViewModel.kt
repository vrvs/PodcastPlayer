package br.ufpe.cin.vrvs.podcastplayer.viewmodel.podcast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import br.ufpe.cin.vrvs.podcastplayer.data.model.Podcast
import br.ufpe.cin.vrvs.podcastplayer.data.model.Result
import br.ufpe.cin.vrvs.podcastplayer.data.repository.PodcastRepository
import br.ufpe.cin.vrvs.podcastplayer.viewmodel.BaseViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PodcastDetailsViewModel : BaseViewModel(), KoinComponent {

    private val podcastRepository: PodcastRepository by inject()

    private val _podcast =  MutableLiveData<Podcast>()
    val podcast: LiveData<Podcast> = _podcast

    private var answer: LiveData<Result<Podcast>>? = null
    private val podcastObserver = Observer<Result<Podcast>> {
        when (it) {
            is Result.Success -> postValues(loading = false, error = false, podcast = it.data)
            is Result.Loading -> postValues(loading = true, error = false, podcast = null)
            is Result.Error -> postValues(loading = false, error = true, podcast = null)
        }
    }

    private var answerSubs: LiveData<Result<String>>? = null
    private val subsObserver = Observer<Result<String>> {
        when (it) {
            is Result.Success -> getPodcast(it.data)
            is Result.Loading -> postValues(loading = true, error = false, podcast = null)
            is Result.Error -> postValues(loading = false, error = true, podcast = null)
        }
    }

    fun getPodcast(id: String) {
        postValues(loading = false, error = false, podcast = null)
        answer?.removeObserver(podcastObserver)
        answer = podcastRepository.getPodcast(id)
        answer?.observeForever(podcastObserver)
    }

    fun subscribePodcast(id: String) {
        postValues(loading = false, error = false, podcast = null)
        answerSubs?.removeObserver(subsObserver)
        answerSubs = podcastRepository.subscribePodcast(id)
        answerSubs?.observeForever(subsObserver)
    }

    fun unsubscribePodcast(id: String) {
        postValues(loading = false, error = false, podcast = null)
        answerSubs?.removeObserver(subsObserver)
        answerSubs = podcastRepository.unsubscribePodcast(id)
        answerSubs?.observeForever(subsObserver)
    }

    override fun onCleared() {
        super.onCleared()
        answer?.removeObserver(podcastObserver)
        answerSubs?.removeObserver(subsObserver)
    }

    private fun postValues(loading: Boolean, error: Boolean, podcast: Podcast?) {
        super.protectedLoading.postValue(loading)
        protectedError.postValue(error)
        podcast?.let{ _podcast.postValue(it) }
    }
}