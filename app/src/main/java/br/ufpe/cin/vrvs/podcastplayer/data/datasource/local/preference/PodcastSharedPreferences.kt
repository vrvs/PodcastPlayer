package br.ufpe.cin.vrvs.podcastplayer.data.datasource.local.preference

interface PodcastSharedPreferences {
    var podcastId: String?
    fun clearPodcastId()
    var podcastTime: Long?
    fun clearPodcastTime()
}