package com.news.newsApp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.news.newsApp.sample.dummyArticlesTest
import com.robo.news.source.network.Resource
import com.robo.news.source.news.NewsApiRepositoryInterface
import com.robo.news.source.news.NewsModel
import com.robo.news.util.GENERAL_ERROR

class FakeNewsApiRepository: NewsApiRepositoryInterface {
    private var articles = NewsModel() //mutableListOf( NewsModel().articles)
    private val successResource = Resource.success(NewsModel("",0, emptyList()));
    private val errorResource = Resource.error("Unauthorised", null)

    private val observableArticles = MutableLiveData<Resource<NewsModel>>(successResource)
    private var networkErrorHappened = false

    fun setNetworkErrorHappened(hasError: Boolean) {
        networkErrorHappened = hasError
    }

    override suspend fun getHeadlines(
        query: String,
        page: Int,
        pageSize: Int
    ): Resource<NewsModel>? {
        TODO("Not yet implemented")
    }

    override fun observeHeadlines(): LiveData<Resource<NewsModel>> {
        TODO("Not yet implemented")
    }

//    override suspend fun getHeadlines(query: String, noOfRec:Int, page:Int): <Resource<NewsModel>>? {
//        return if(networkErrorHappened) {
//            errorResource
//        } else {
////            val newsData = NewsModel("Success" , 5 , dummyArticlesTest)
////            articles =  newsData
//            observableArticles.postValue(successResource)
//            null
//        }
//    }
//
//    override fun observeHeadlines(): LiveData<<Resource<NewsModel>>> {
//        return observableArticles
//    }
}