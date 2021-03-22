package br.ufpe.cin.vrvs.podcastplayer.data.datasource.remote.podcastindex.response

import br.ufpe.cin.vrvs.podcastplayer.data.model.Episode
import br.ufpe.cin.vrvs.podcastplayer.data.model.Episode.*
import com.google.gson.annotations.SerializedName

class EpisodeResponse {

    @SerializedName("id")
    var id: Int = -1

    @SerializedName("feedId")
    var podcastId: Int = -1

    @SerializedName("title")
    lateinit var title: String

    @SerializedName("description")
    lateinit var description: String

    @SerializedName("enclosureUrl")
    lateinit var audioUrl: String

    @SerializedName("feedImage")
    lateinit var imageUrl: String

    @SerializedName("datePublished")
    var datePublished: Long = 0L

    @SerializedName("duration")
    var duration: Int = 0

    @SerializedName("episode")
    var episode: Int = -1

    @SerializedName("season")
    var season: Int = -1
}

class EpisodesResponse {

    @SerializedName("items")
    lateinit var episodes: List<EpisodeResponse>
}

fun Companion.toEpisode(episodeResponse: EpisodeResponse) = Episode(
    id = episodeResponse.id.toString(),
    podcastId = episodeResponse.podcastId.toString(),
    title = episodeResponse.title,
    description = episodeResponse.description,
    audioUrl = episodeResponse.audioUrl,
    imageUrl = episodeResponse.imageUrl,
    datePublished = episodeResponse.datePublished,
    duration = episodeResponse.duration,
    episode = episodeResponse.episode,
    season = episodeResponse.season
)