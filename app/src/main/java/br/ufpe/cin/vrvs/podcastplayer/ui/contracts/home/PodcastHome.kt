package br.ufpe.cin.vrvs.podcastplayer.ui.contracts.home

import br.ufpe.cin.vrvs.podcastplayer.data.model.Podcast

class PodcastHome {

    sealed class Intent {
        object GetSubscribedPodcasts : Intent()
    }

    data class State(
        val loading: Boolean,
        val error: String?,
        val data: List<Podcast>?
    )
}