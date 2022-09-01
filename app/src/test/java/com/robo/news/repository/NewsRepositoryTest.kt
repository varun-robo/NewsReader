package com.github.harmittaa.koinexample.repository

import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
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
import org.mockito.Mockito.anyString
import retrofit2.HttpException

@RunWith(JUnit4::class)
class NewsRepositoryTest {
    private val responseHandler = ResponseHandler()
    private lateinit var newsApi: ApiClient
    private lateinit var repository: NewsRepository
    private val validLocation = "Helsinki"
    private val invalidLocation = "Somewhere else"
    private val newsModel = NewsModel("Success",0, emptyList())
    private val newsResponse = Resource.success(newsModel)
    private val errorResponse = Resource.error("Unauthorised", null)


    @Before
    fun setUp() {
        newsApi = mock()
        val mockException: HttpException = mock()
        whenever(mockException.code()).thenReturn(401)
        runBlocking {
            whenever(newsApi.fetchNews(eq(invalidLocation), anyString())).thenThrow(mockException)
            whenever(newsApi.fetchNews(eq(validLocation), anyString())).thenReturn(newsModel)
        }
        repository = NewsRepository(
            newsApi)
    }

    @Test
    fun `test getWeather when valid location is requested, then weather is returned`() =
        runBlocking {
            assertEquals(newsResponse, repository.getHeadlines("iPhone" , 1, 6))
        }

    @Test
    fun `test getWeather when invalid location is requested, then error is returned`() =
        runBlocking {
            assertEquals(errorResponse, repository.getHeadlines("varun" , 1, 6))
        }
}