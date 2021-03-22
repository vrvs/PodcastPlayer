package br.ufpe.cin.vrvs.podcastplayer.data.datasource.remote.podcastindex.response

import br.ufpe.cin.vrvs.podcastplayer.data.model.Podcast
import br.ufpe.cin.vrvs.podcastplayer.data.model.Podcast.*
import com.google.gson.annotations.SerializedName

open class PodcastRecentResponse {

    @SerializedName("id")
    var id: Int = -1

    @SerializedName("title")
    lateinit var title: String

    @SerializedName("image")
    lateinit var imageUrl: String

    @SerializedName("categories")
    lateinit var categories: Map<String, String>
}

class PodcastsRecentResponse {

    @SerializedName("feeds")
    lateinit var feeds: List<PodcastRecentResponse>
}

fun Companion.toPodcast(podcastRecentResponse: PodcastRecentResponse) = Podcast(
    id = podcastRecentResponse.id.toString(),
    title = podcastRecentResponse.title,
    imageUrl = podcastRecentResponse.imageUrl,
    categories = podcastRecentResponse.categories
)