package com.robo.news.source.news

import androidx.lifecycle.LiveData
import com.robo.news.BuildConfig
import com.robo.news.source.network.ApiClient
import com.robo.news.source.network.Resource
import com.robo.news.source.network.ResponseHandler
import org.koin.dsl.module
import java.io.IOException

val repositoryModule = module {
    factory { NewsRepository( get() , get()) }
}

class NewsRepository(
    private val api: ApiClient,
    val db: NewsDao?
):NewsApiRepositoryInterface {
    constructor( api: ApiClient) : this(api, null) {

    }
    val responseHandler: ResponseHandler = ResponseHandler()


    override  suspend fun getHeadlines(
        query: String,
        page: Int,
        pageSize: Int,
    ): Resource<NewsModel> {
        var res : Resource<NewsModel> = responseHandler.handleException(IOException())

         runCatching{
           // ResponseHandler.Loading()
            ResponseHandler().handleSuccess( api.fetchNews(
                BuildConfig.API_KEY,
                "in",
                query,
                page,
                pageSize
            ))
        }.onSuccess{
             res = it
        }
             //.recover { "" }
             .onFailure {
                 res = responseHandler.handleException(IOException())
        }
        return res
    }

    suspend fun find(articleModel: ArticleModel)= db?.find(articleModel.publishedAt )

    suspend fun save(articleModel: ArticleModel){
        db?.save(articleModel)
    }

    suspend fun remove(articleModel: ArticleModel){
        db?.remove(articleModel)
    }

    override fun observeHeadlines(): LiveData<Resource<NewsModel>> {
        TODO("Not yet implemented")
    }
}