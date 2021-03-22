package br.ufpe.cin.vrvs.podcastplayer.data.datasource.remote.podcastindex.response

import br.ufpe.cin.vrvs.podcastplayer.data.model.Podcast
import br.ufpe.cin.vrvs.podcastplayer.data.model.Podcast.*
import com.google.gson.annotations.SerializedName

class PodcastCompleteResponse: PodcastRecentResponse() {

    @SerializedName("author")
    lateinit var author: String

    @SerializedName("description")
    lateinit var description: String
}

class PodcastsSearchResponse {

    @SerializedName("feeds")
    lateinit var feeds: List<PodcastCompleteResponse>
}

class PodcastByIdResponse {

    @SerializedName("feed")
    lateinit var feed: PodcastCompleteResponse
}

fun Companion.toPodcast(podcastCompleteResponse: PodcastCompleteResponse) : Podcast {
    val result = Podcast.toPodcast(podcastCompleteResponse as PodcastRecentResponse)
    result.author = podcastCompleteResponse.author
    result.description = podcastCompleteResponse.description
    return result
}