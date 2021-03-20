package br.ufpe.cin.vrvs.podcastplayer.data.data_source.remote.podcast_index.response

import com.google.gson.annotations.SerializedName

class EpisodeResponse {

    @SerializedName("id")
    var id: Int = -1

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