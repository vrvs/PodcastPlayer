package br.ufpe.cin.vrvs.podcastplayer.data.datasource.local.database.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

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