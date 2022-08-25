package com.robo.news.ui.bookmark

import androidx.lifecycle.ViewModel
import com.robo.news.source.news.NewsRepository
import org.koin.dsl.module

val bookmarkViewModel = module {
    factory { BookmarkViewModel(get()) }
}

class BookmarkViewModel(
    val repository: NewsRepository
): ViewModel() {

    val title = "Bookmark"
    val articles = repository.db.findAll()
}