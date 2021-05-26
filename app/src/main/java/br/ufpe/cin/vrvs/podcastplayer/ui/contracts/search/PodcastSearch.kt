package br.ufpe.cin.vrvs.podcastplayer.ui.contracts.search

import br.ufpe.cin.vrvs.podcastplayer.data.model.Podcast

class PodcastSearch {

    sealed class Intent {
        object GetPodcastFeed : Intent()
        data class SearchPodcasts(val query: String) : Intent()
    }

    data class State(
        val loading: Boolean,
        val error: String?,
        val data: List<Podcast>?
    )
}