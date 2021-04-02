package br.ufpe.cin.vrvs.podcastplayer.viewmodel.podcasts.search

import br.ufpe.cin.vrvs.podcastplayer.viewmodel.podcasts.BasePodcastsViewModel

class SearchPodcastsViewModel : BasePodcastsViewModel() {

    fun searchPodcasts(query: String) {
        postValues(loading = false, error = false, podcasts = emptyList())
        answer?.removeObserver(podcastObserver)
        answer = podcastRepository.searchPodcast(query)
        answer?.observeForever(podcastObserver)
    }

    fun getPodcastsFeed() {
        postValues(loading = false, error = false, podcasts = emptyList())
        answer?.removeObserver(podcastObserver)
        answer = podcastRepository.getPodcastFeed()
        answer?.observeForever(podcastObserver)
    }
}