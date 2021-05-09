package com.koa2.omdb.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_bookmarks")
data class MovieBookMarkData(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    @ColumnInfo(name = "movieId")
    val movieId: String,
    @ColumnInfo(name = "movieTitle")
    val movieTitle: String,
    @ColumnInfo(name = "moviePoster")
    val moviePoster: String,
)