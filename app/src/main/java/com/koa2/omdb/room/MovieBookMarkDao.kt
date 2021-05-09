package com.koa2.omdb.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieBookMarkDao {

    @Query("select * from movie_bookmarks order by uid desc")
    fun getAllMovies(): Flow<List<MovieBookMarkData>>

    @Query("select * from movie_bookmarks order by uid desc")
    suspend fun getAllMoviesInline(): List<MovieBookMarkData>

    @Query("select movieId from movie_bookmarks where movieId = :movieId")
    suspend fun isBookMarked(movieId: String): String?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMovie(movie: MovieBookMarkData)

    @Query("delete from movie_bookmarks where movieId =:movieId ")
    suspend fun deleteMovie(movieId: String)
}