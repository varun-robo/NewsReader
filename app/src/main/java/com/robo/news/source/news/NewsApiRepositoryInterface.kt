package com.robo.news.source.news

import androidx.lifecycle.LiveData


interface NewsApiRepositoryInterface {
    suspend fun getHeadlines(query: String,
                             page: Int,
                             pageSize: Int): NewsModel?
    fun observeHeadlines() : LiveData<NewsModel>
}