package com.koa2.omdb.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MovieBookMarkData::class], version = 1)
abstract class MovieDataBase : RoomDatabase() {
    abstract fun moviesDao(): MovieBookMarkDao
}