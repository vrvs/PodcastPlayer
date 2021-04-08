package br.ufpe.cin.vrvs.podcastplayer.ui.contracts.podcast

import br.ufpe.cin.vrvs.podcastplayer.data.model.Podcast

class PodcastDetails {

    sealed class Intent {
        data class GetPodcast(val id: String) : Intent()
        data class UpdatePodcast(val id: String) : Intent()
        data class SubscribePodcast(val id: String) : Intent() {
            companion object {
                @JvmStatic
                fun create(id: String) = SubscribePodcast(id)
            }
        }
        data class UnsubscribePodcast(val id: String) : Intent() {
            companion object {
                @JvmStatic
                fun create(id: String) = UnsubscribePodcast(id)
            }
        }
    }

    data class State(
        val loading: Boolean,
        val error: String?,
        val data: Podcast?
    )
}