package com.robo.news.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.robo.news.source.network.Resource
import com.robo.news.source.news.NewsModel
import com.robo.news.source.news.NewsRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
internal class HomeViewModelTest {
    private var articles = NewsModel()
    private lateinit var viewModel: HomeViewModel
    private lateinit var newsRepository: NewsRepository
    private lateinit var weatherObserver: Observer<NewsModel>
    private val validLocation = "Success"
    private val invalidLocation = "Fail"

    private val successResource = Resource.success(NewsModel("Success",0, emptyList()));
    private val errorResource = Resource.error("Unauthorised", null)


    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        newsRepository = mock()
        runBlocking {
            whenever(newsRepository.getHeadlines("",1,6)).thenReturn(successResource.data)
            whenever(newsRepository.getHeadlines("",1,6)).thenReturn(errorResource.data)
        }
        viewModel = HomeViewModel(newsRepository)
        weatherObserver = mock()
    }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `when getWeather is called with valid location, then observer is updated with success`() = runBlocking {
        viewModel.news.observeForever(weatherObserver)
        viewModel.fetch()
        delay(10)
        verify(newsRepository).getHeadlines("Success",1,6)
        verify(weatherObserver, timeout(50)).onChanged(Resource.loading(null).data)
        verify(weatherObserver, timeout(50)).onChanged(successResource.data)
    }

    @Test
    fun `when getWeather is called with invalid location, then observer is updated with failure`() = runBlocking {
        viewModel.news.observeForever(weatherObserver)
        viewModel.fetch()
        delay(10)
        verify(newsRepository).getHeadlines("Error",1,6)
        verify(weatherObserver, timeout(50)).onChanged(Resource.loading(null).data)
        verify(weatherObserver, timeout(50)).onChanged(errorResource.data)
    }


    @org.junit.jupiter.api.Test
    fun getMessage() {
    }

    @org.junit.jupiter.api.Test
    fun getNews() {
    }

    @org.junit.jupiter.api.Test
    fun fetch() {
    }

    @org.junit.jupiter.api.Test
    fun generateToken() {
    }

    @org.junit.jupiter.api.Test
    fun getRepository() {
    }
}