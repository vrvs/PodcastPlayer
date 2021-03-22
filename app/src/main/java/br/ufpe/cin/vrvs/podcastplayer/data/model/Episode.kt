package br.ufpe.cin.vrvs.podcastplayer.data.model

data class Episode(
    var id: String,
    var podcastId: String,
    var title: String,
    var description: String,
    var audioUrl: String,
    var imageUrl: String,
    var datePublished: Long,
    var duration: Int,
    var episode: Int,
    var season: Int,
    var downloaded: Boolean = false,
    var path: String = ""
) {
    companion object
}