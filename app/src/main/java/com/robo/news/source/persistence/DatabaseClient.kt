package com.robo.news.source.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.robo.news.source.news.ArticleModel
import com.robo.news.source.news.NewsDao
import com.robo.news.util.SourceConverter

@Database(
    entities = [ArticleModel::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(SourceConverter::class)
abstract class DatabaseClient : RoomDatabase() {
    abstract val newsDao: NewsDao
}