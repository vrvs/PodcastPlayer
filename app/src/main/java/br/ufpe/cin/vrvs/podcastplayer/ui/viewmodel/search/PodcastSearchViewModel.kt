package br.ufpe.cin.vrvs.podcastplayer.ui.viewmodel.search

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import br.ufpe.cin.vrvs.podcastplayer.data.model.Podcast
import br.ufpe.cin.vrvs.podcastplayer.data.model.Result
import br.ufpe.cin.vrvs.podcastplayer.data.repository.PodcastRepository
import br.ufpe.cin.vrvs.podcastplayer.ui.contracts.search.PodcastSearch
import br.ufpe.cin.vrvs.podcastplayer.ui.viewmodel.BaseViewModel
import br.ufpe.cin.vrvs.podcastplayer.utils.Utils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PodcastSearchViewModel(context: Context) :
    BaseViewModel<PodcastSearch.Intent, PodcastSearch.State>(PodcastSearch.State(false, null, null)), KoinComponent {

    private val podcastRepository: PodcastRepository by inject()

    init {
        handleIntents()
    }

    private fun handleIntents() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is PodcastSearch.Intent.GetPodcastFeed -> getPodcastsFeed()
                    is PodcastSearch.Intent.SearchPodcasts -> searchPodcasts(intent.query)
                }
            }
        }
    }

    private var answer: LiveData<Result<List<Podcast>>>? = null
    private val podcastObserver = Observer<Result<List<Podcast>>> {
        when (it) {
            is Result.Success -> postValues(loading = false, error = null, podcasts = it.data)
            is Result.Loading -> postValues(loading = true, error = null, podcasts = emptyList())
            is Result.Error -> postValues(loading = false, error = Utils.getString(context, it.error), podcasts = null)
        }
    }

    private fun searchPodcasts(query: String) {
        postValues(loading = false, error = null, podcasts = null)
        answer?.removeObserver(podcastObserver)
        answer = podcastRepository.searchPodcast(query)
        answer?.observeForever(podcastObserver)
    }

    private fun getPodcastsFeed() {
        postValues(loading = false, error = null, podcasts = null)
        answer?.removeObserver(podcastObserver)
        answer = podcastRepository.getPodcastFeed()
        answer?.observeForever(podcastObserver)
    }

    override fun onCleared() {
        super.onCleared()
        answer?.removeObserver(podcastObserver)
    }

    private fun postValues(loading: Boolean, error: String?, podcasts: List<Podcast>?) {
        protectedState.postValue(PodcastSearch.State(loading, error, podcasts))
    }
}