package br.ufpe.cin.vrvs.podcastplayer.data.datasource.remote.podcastindex.response

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