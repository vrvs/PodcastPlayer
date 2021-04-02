package br.ufpe.cin.vrvs.podcastplayer.viewmodel.podcast

class SubscribedPodcastViewModel : BasePodcastViewModel() {

    fun refreshSubscribedPodcast() {
        postValues(loading = false, error = false, podcasts = emptyList())
        answer?.removeObserver(podcastObserver)
        answer = podcastRepository.getSubscribedPodcast()
        answer?.observeForever(podcastObserver)
    }
}