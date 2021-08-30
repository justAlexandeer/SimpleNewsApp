package com.github.justalexandeer.simplenewsapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.justalexandeer.simplenewsapp.data.db.entity.RemoteKeys

@Dao
interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_key WHERE articleId  =:articleId")
    suspend fun remoteKeysRepoId(articleId: Long): RemoteKeys?

    @Query("DELETE FROM remote_key")
    suspend fun clearRemoteKeys()
}