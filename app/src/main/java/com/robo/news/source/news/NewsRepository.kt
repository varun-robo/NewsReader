package com.robo.news.source.news

import androidx.lifecycle.LiveData
import com.robo.news.BuildConfig
import com.robo.news.source.network.ApiClient
import org.koin.dsl.module

val repositoryModule = module {
    factory { NewsRepository( get() , get()) }
}

class NewsRepository(
    private val api: ApiClient,
    val db: NewsDao?
):NewsApiRepositoryInterface {
    constructor( api: ApiClient) : this(api, null) {

    }

    override  suspend fun getHeadlines(
        query: String,
        page: Int,
        pageSize: Int,
    ): NewsModel{
        return api.fetchNews(
            BuildConfig.API_KEY,
            "id",
            query,
            page,
            pageSize
        )
    }

    suspend fun find(articleModel: ArticleModel)= db?.find(articleModel.publishedAt )

    suspend fun save(articleModel: ArticleModel){
        db?.save(articleModel)
    }

    suspend fun remove(articleModel: ArticleModel){
        db?.remove(articleModel)
    }

    override fun observeHeadlines(): LiveData<NewsModel> {
        TODO("Not yet implemented")
    }
}