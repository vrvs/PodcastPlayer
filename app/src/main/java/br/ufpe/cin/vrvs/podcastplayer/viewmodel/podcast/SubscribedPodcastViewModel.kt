package br.ufpe.cin.vrvs.podcastplayer.viewmodel.podcast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import br.ufpe.cin.vrvs.podcastplayer.data.model.Podcast
import br.ufpe.cin.vrvs.podcastplayer.data.model.Result
import br.ufpe.cin.vrvs.podcastplayer.data.repository.PodcastRepository
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class SubscribedPodcastViewModel : ViewModel(), KoinComponent {

    private val podcastRepository: PodcastRepository by inject()

    private val _podcasts =  MutableLiveData<List<Podcast>>()
    val podcasts: LiveData<List<Podcast>> = _podcasts

    private val _loading =  MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error =  MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private var answer: LiveData<Result<List<Podcast>>>? = null
    private val podcastObserver = Observer<Result<List<Podcast>>> {
        when (it) {
            is Result.Success -> postValues(loading = false, error = false, podcasts = it.data)
            is Result.Loading -> postValues(loading = true, error = false, podcasts = emptyList())
            is Result.Error -> postValues(loading = false, error = true, podcasts = emptyList())
        }
    }

    fun refreshSubscribedPodcast() {
        postValues(loading = false, error = false, podcasts = emptyList())
        answer?.removeObserver(podcastObserver)
        answer = podcastRepository.getPodcastFeed()
        answer?.observeForever(podcastObserver)
    }

    override fun onCleared() {
        super.onCleared()
        answer?.removeObserver(podcastObserver)
    }

    private fun postValues(loading: Boolean, error: Boolean, podcasts: List<Podcast>) {
        _loading.postValue(loading)
        _error.postValue(error)
        _podcasts.postValue(podcasts)
    }
}