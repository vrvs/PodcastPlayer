package br.ufpe.cin.vrvs.podcastplayer.viewmodel.podcasts.subscribed

import br.ufpe.cin.vrvs.podcastplayer.viewmodel.podcasts.BasePodcastsViewModel

class SubscribedPodcastsViewModel : BasePodcastsViewModel() {

    fun refreshSubscribedPodcast() {
        postValues(loading = false, error = false, podcasts = emptyList())
        answer?.removeObserver(podcastObserver)
        answer = podcastRepository.getSubscribedPodcast()
        answer?.observeForever(podcastObserver)
    }
}