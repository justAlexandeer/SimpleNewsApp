package com.github.justalexandeer.simplenewsapp.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.justalexandeer.simplenewsapp.data.db.entity.ArticleDb
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<ArticleDb>)

    @Query("SELECT * FROM article WHERE " +
            "query LIKE :queryString " +
            "ORDER BY idArticle ASC")
    fun articlesByQuery(queryString: String): PagingSource<Int, ArticleDb>

    @Query("DELETE FROM article")
    suspend fun clearArticles()

    @Query("SELECT * from article")
    fun getAllArticle(): Flow<List<ArticleDb>>

    @Insert
    suspend fun insertAllMainArticle(articles: List<ArticleDb>)
}