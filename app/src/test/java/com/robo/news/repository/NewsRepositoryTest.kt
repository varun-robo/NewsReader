package com.robo.news.repository

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.robo.news.BuildConfig
import com.robo.news.source.network.ApiClient
import com.robo.news.source.network.Resource
import com.robo.news.source.network.ResponseHandler
import com.robo.news.source.news.NewsModel
import com.robo.news.source.news.NewsRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException

@RunWith(JUnit4::class)
class NewsRepositoryTest {
    private val responseHandler = ResponseHandler()
    private lateinit var newsApi: ApiClient
    private lateinit var repository: NewsRepository
    private val newsModel = NewsModel("Success",0, emptyList())
    private val newsResponse = Resource.success(newsModel)
    private val errorResponse = Resource.error("Something went wrong", null)


    @Before
    fun setUp() {
        newsApi = mock()
        val mockException: HttpException = mock()
        whenever(mockException.code()).thenReturn(401)
        runBlocking {
            // correct api query  param call
            whenever(newsApi.fetchNews(
                BuildConfig.API_KEY,
                "in",
                "Success",
                1,
                5)).thenReturn(newsModel)

            // wrong api query  param call
            whenever(newsApi.fetchNews(BuildConfig.API_KEY,
                "in",
                "Error",
                1,
                5)).thenThrow(mockException)
        }
        repository = NewsRepository(
            newsApi)
    }

    @Test
    fun `test getWeather when valid search is requested, then weather is returned`() =
        runBlocking {
            assertEquals(newsResponse, repository.getHeadlines("Error" , 1, 6))
        }

    @Test
    fun `test getWeather when invalid search is requested, then error is returned`() =
        runBlocking {
            assertEquals(errorResponse, repository.getHeadlines("Success" , 1, 6))
        }
}