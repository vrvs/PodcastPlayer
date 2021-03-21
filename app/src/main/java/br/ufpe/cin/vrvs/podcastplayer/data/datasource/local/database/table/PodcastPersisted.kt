package br.ufpe.cin.vrvs.podcastplayer.data.datasource.local.database.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "podcast_table")
data class PodcastPersisted(
    @PrimaryKey(autoGenerate = false) var id: String,
    @ColumnInfo(name = "author") var author: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "imageUrl") var imageUrl: String,
    @ColumnInfo(name = "categories") var categories: Map<String, String>,
    @ColumnInfo(name = "subscribed") var subscribed: Boolean = false
)