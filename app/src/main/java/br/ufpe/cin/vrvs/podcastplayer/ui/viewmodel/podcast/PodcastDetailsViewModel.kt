package br.ufpe.cin.vrvs.podcastplayer.ui.viewmodel.podcast

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import br.ufpe.cin.vrvs.podcastplayer.data.model.Podcast
import br.ufpe.cin.vrvs.podcastplayer.data.model.Result
import br.ufpe.cin.vrvs.podcastplayer.data.repository.PodcastRepository
import br.ufpe.cin.vrvs.podcastplayer.ui.contracts.podcast.PodcastDetails
import br.ufpe.cin.vrvs.podcastplayer.ui.contracts.podcast.PodcastDetails.Intent.GetPodcast
import br.ufpe.cin.vrvs.podcastplayer.ui.contracts.podcast.PodcastDetails.Intent.SubscribePodcast
import br.ufpe.cin.vrvs.podcastplayer.ui.contracts.podcast.PodcastDetails.Intent.UnsubscribePodcast
import br.ufpe.cin.vrvs.podcastplayer.ui.contracts.podcast.PodcastDetails.Intent.UpdatePodcast
import br.ufpe.cin.vrvs.podcastplayer.utils.Utils
import br.ufpe.cin.vrvs.podcastplayer.ui.viewmodel.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PodcastDetailsViewModel(context: Context) :
    BaseViewModel<PodcastDetails.Intent, PodcastDetails.State>(PodcastDetails.State(false, null, null)), KoinComponent {

    private val podcastRepository: PodcastRepository by inject()

    init {
        handleIntents()
    }

    private fun handleIntents() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is GetPodcast -> getPodcast(intent.id)
                    is UpdatePodcast -> updatePodcast(intent.id)
                    is SubscribePodcast -> subscribePodcast(intent.id)
                    is UnsubscribePodcast -> unsubscribePodcast(intent.id)
                }
            }
        }
    }

    private var answer: LiveData<Result<Podcast>>? = null
    private val podcastObserver = Observer<Result<Podcast>> {
        when (it) {
            is Result.Success -> postValues(loading = false, error = null, podcast = it.data)
            is Result.Loading -> postValues(loading = true, error = null, podcast = null)
            is Result.Error -> postValues(loading = false, error = Utils.getString(context, it.error), podcast = null)
        }
    }
    private val podcastUpdateObserver = Observer<Result<Podcast>> {
        when (it) {
            is Result.Success -> postValues(loading = false, error = null, podcast = it.data)
            is Result.Loading -> {}
            is Result.Error -> postValues(loading = false, error = Utils.getString(context, it.error), podcast = null)
        }
    }

    private var answerSubs: LiveData<Result<String>>? = null
    private val subsObserver = Observer<Result<String>> {
        when (it) {
            is Result.Success -> getPodcast(it.data)
            is Result.Loading -> postValues(loading = true, error = null, podcast = null)
            is Result.Error -> postValues(loading = false, error = Utils.getString(context, it.error), podcast = null)
        }
    }

    private fun getPodcast(id: String) {
        postValues(loading = false, error = null, podcast = null)
        answer?.removeObserver(podcastObserver)
        answer = podcastRepository.getPodcast(id)
        answer?.observeForever(podcastObserver)
    }

    private fun updatePodcast(id: String) {
        postValues(loading = false, error = null, podcast = null)
        answer?.removeObserver(podcastUpdateObserver)
        answer = podcastRepository.getPodcast(id)
        answer?.observeForever(podcastUpdateObserver)
    }

    private fun subscribePodcast(id: String) {
        postValues(loading = false, error = null, podcast = null)
        answerSubs?.removeObserver(subsObserver)
        answerSubs = podcastRepository.subscribePodcast(id)
        answerSubs?.observeForever(subsObserver)
    }

    private fun unsubscribePodcast(id: String) {
        postValues(loading = false, error = null, podcast = null)
        answerSubs?.removeObserver(subsObserver)
        answerSubs = podcastRepository.unsubscribePodcast(id)
        answerSubs?.observeForever(subsObserver)
    }

    override fun onCleared() {
        super.onCleared()
        answer?.removeObserver(podcastObserver)
        answerSubs?.removeObserver(subsObserver)
    }

    private fun postValues(loading: Boolean, error: String?, podcast: Podcast?) {
        protectedState.postValue(PodcastDetails.State(loading, error, podcast))
    }
}