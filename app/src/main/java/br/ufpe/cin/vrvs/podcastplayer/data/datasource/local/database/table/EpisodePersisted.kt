package br.ufpe.cin.vrvs.podcastplayer.data.datasource.local.database.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import br.ufpe.cin.vrvs.podcastplayer.data.model.Episode

@Entity(
    tableName = "episode_table",
    foreignKeys = [ForeignKey(
        entity = PodcastPersisted::class,
        parentColumns = ["id"],
        childColumns = ["podcastId"],
        onDelete = CASCADE
    )]
)
data class EpisodePersisted(
    @PrimaryKey(autoGenerate = false) var id: String,
    @ColumnInfo(name = "podcastId") var podcastId: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "audioUrl") var audioUrl: String,
    @ColumnInfo(name = "imageUrl") var imageUrl: String,
    @ColumnInfo(name = "datePublished") var datePublished: Long,
    @ColumnInfo(name = "duration") var duration: Int,
    @ColumnInfo(name = "episode") var episode: Int,
    @ColumnInfo(name = "season") var season: Int,
    @ColumnInfo(name = "downloaded") var downloaded: Boolean = false,
    @ColumnInfo(name = "path") var path: String = ""
)

@Entity
data class EpisodePersistedDownloaded(
    @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "downloaded") var downloaded: Boolean = false,
    @ColumnInfo(name = "path") var path: String = ""
)

fun Episode.Companion.toEpisode(episodePersisted: EpisodePersisted) = Episode(
    id = episodePersisted.id,
    podcastId = episodePersisted.podcastId,
    title = episodePersisted.title,
    description = episodePersisted.description,
    audioUrl = episodePersisted.audioUrl,
    imageUrl = episodePersisted.imageUrl,
    datePublished = episodePersisted.datePublished,
    duration = episodePersisted.duration,
    episode = episodePersisted.episode,
    season = episodePersisted.season,
    downloaded = episodePersisted.downloaded,
    path = episodePersisted.path
)

fun Episode.Companion.fromEpisode(episode: Episode) = EpisodePersisted(
    id = episode.id,
    podcastId = episode.podcastId,
    title = episode.title,
    description = episode.description,
    audioUrl = episode.audioUrl,
    imageUrl = episode.imageUrl,
    datePublished = episode.datePublished,
    duration = episode.duration,
    episode = episode.episode,
    season = episode.season,
    downloaded = episode.downloaded,
    path = episode.path
)