package br.ufpe.cin.vrvs.podcastplayer.data.datasource.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.ufpe.cin.vrvs.podcastplayer.data.datasource.local.database.table.EpisodePersisted
import br.ufpe.cin.vrvs.podcastplayer.data.datasource.local.database.table.PodcastPersisted

@Dao
interface PodcastDao {

    @Query("SELECT * FROM podcast_table")
    suspend fun getPodcasts(): List<PodcastPersisted>

    @Query("SELECT * FROM podcast_table where :id = id")
    suspend fun getPodcast(id: String): PodcastPersisted

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPodcast(podcast: PodcastPersisted)

    @Query("DELETE FROM podcast_table where :id = id")
    suspend fun clearPodcast(id: String)

    @Query("SELECT * FROM episode_table")
    suspend fun getEpisodes(): List<EpisodePersisted>

    @Query("SELECT * FROM episode_table where :id = id")
    suspend fun getEpisode(id: String): EpisodePersisted

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEpisode(episode: EpisodePersisted)

    @Query("DELETE FROM episode_table where :id = id")
    suspend fun clearEpisode(id: String)
}
