package com.robo.news.source.news

import androidx.lifecycle.LiveData
import com.robo.news.source.network.Resource


interface NewsApiRepositoryInterface {
    suspend fun getHeadlines(query: String,
                             page: Int,
                             pageSize: Int): Resource<NewsModel>?
    fun observeHeadlines() : LiveData<Resource<NewsModel>>
}