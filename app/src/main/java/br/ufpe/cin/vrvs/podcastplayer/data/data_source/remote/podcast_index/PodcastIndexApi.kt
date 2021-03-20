package br.ufpe.cin.vrvs.podcastplayer.data.data_source.remote.podcast_index

import br.ufpe.cin.vrvs.podcastplayer.data.data_source.remote.podcast_index.response.EpisodesResponse
import br.ufpe.cin.vrvs.podcastplayer.data.data_source.remote.podcast_index.response.PodcastByIdResponse
import br.ufpe.cin.vrvs.podcastplayer.data.data_source.remote.podcast_index.response.PodcastsRecentResponse
import br.ufpe.cin.vrvs.podcastplayer.data.data_source.remote.podcast_index.response.PodcastsSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import retrofit2.http.QueryName

interface PodcastIndexApi {

    companion object {
        const val URL = "https://api.podcastindex.org/api/1.0/"
    }

    @Headers("Accept: application/json")
    @GET("recent/feeds")
    fun getRecentFeed(
        @Query("max") max: Int,
        @QueryName pretty: String = "pretty"
    ) : Call<PodcastsRecentResponse>

    @Headers("Accept: application/json")
    @GET("podcasts/byfeedid")
    fun getPodcast(
        @Query("id") id: Int,
        @QueryName pretty: String = "pretty"
    ) : Call<PodcastByIdResponse>

    @Headers("Accept: application/json")
    @GET("search/byterm")
    fun searchPodcasts(
        @Query("q") query: String,
        @QueryName pretty: String = "pretty"
    ) : Call<PodcastsSearchResponse>

    @Headers("Accept: application/json")
    @GET("search/byterm")
    fun searchEpisodes(
        @Query("id") podcastId: String,
        @QueryName pretty: String = "pretty"
    ) : Call<EpisodesResponse>
}