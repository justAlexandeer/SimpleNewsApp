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
    suspend fun insertAll(articles: List<ArticleDb>): List<Long>

    @Query(
        "SELECT * FROM article WHERE " +
                "query LIKE :queryString " +
                "AND type = :type " +
                "ORDER BY idArticle ASC"
    )
    fun articlesByQuery(type: String, queryString: String): PagingSource<Int, ArticleDb>

    @Query("DELETE FROM article WHERE type = :type")
    suspend fun clearArticles(type: String)

    @Query("DELETE FROM article WHERE type = :type AND `query` = :queryString")
    suspend fun clearArticles(type: String, queryString: String)

    @Query("SELECT * from article WHERE type = :type AND `query` = :queryString")
    fun getAllArticle(type: String, queryString: String): Flow<List<ArticleDb>>

    @Insert
    suspend fun insertAllMainArticle(articles: List<ArticleDb>)

    @Query(
        "SELECT * from article WHERE type = :type AND title = :title " +
                "AND content = :content AND url = :url"
    )
    fun getArticleInFavorite(
        type: String,
        title: String,
        content: String,
        url: String
    ): Flow<ArticleDb?>

    @Query(
        "DELETE FROM article WHERE type = :type AND title = :title " +
                "AND content = :content AND url = :url"
    )
    suspend fun clearArticleFromFavorite(
        type: String, title: String,
        content: String,
        url: String
    )

}