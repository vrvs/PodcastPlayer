package br.ufpe.cin.vrvs.podcastplayer.data.datasource.remote.podcastindex.response

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