package com.news.newsApp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.news.newsApp.sample.dummyArticlesTest
import com.robo.news.source.news.NewsApiRepositoryInterface
import com.robo.news.source.news.NewsModel
import com.robo.news.util.GENERAL_ERROR

class FakeNewsApiRepository: NewsApiRepositoryInterface {
    private var articles = NewsModel() //mutableListOf( NewsModel().articles)
    private val observableArticles = MutableLiveData<NewsModel>(articles)
    private var networkErrorHappened = false

    fun setNetworkErrorHappened(hasError: Boolean) {
        networkErrorHappened = hasError
    }

    override suspend fun getHeadlines(query: String, noOfRec:Int, page:Int): NewsModel? {
        return if(networkErrorHappened) {
            NewsModel("Fail" , 0 , emptyList())
        } else {
            val newsData = NewsModel("Success" , 5 , dummyArticlesTest)
            articles =  newsData
            observableArticles.postValue(articles)
            null
        }
    }

    override fun observeHeadlines(): LiveData<NewsModel> {
        return observableArticles
    }
}