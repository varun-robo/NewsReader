package com.robo.news.viewmodel.home.HomeViewModelTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.robo.news.source.network.Resource
import com.robo.news.source.news.NewsModel
import com.robo.news.source.news.NewsRepository
import com.robo.news.ui.home.HomeViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
internal class HomeViewModelTest {
    private var articles = NewsModel()
    private lateinit var viewModel: HomeViewModel
    private lateinit var newsRepository: NewsRepository
    private lateinit var newsObserver: Observer<NewsModel>
    private val validLocation = "Success"
    private val invalidLocation = "Fail"

    private val successResource = Resource.success(NewsModel("",0, emptyList()));
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
            whenever(newsRepository.getHeadlines("",1,5)).thenReturn(successResource.data)
            whenever(newsRepository.getHeadlines("",1,5)).thenReturn(errorResource.data)
        }
        viewModel = HomeViewModel(newsRepository)
        newsObserver = mock()
    }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `when getHeadlines is called with valid location, then observer is updated with success`() = runBlocking {
        viewModel.news.observeForever(newsObserver)
        viewModel.fetch()
        delay(10)
        verify(newsRepository).getHeadlines("",1,5)
        verify(newsObserver, timeout(50)).onChanged(Resource.loading(null).data)
        verify(newsObserver, timeout(50)).onChanged(successResource.data)
    }

    @Test
    fun `when getHeadlines is called with invalid location, then observer is updated with failure`() = runBlocking {
        viewModel.news.observeForever(newsObserver)
        viewModel.fetch()
        delay(10)
        verify(newsRepository).getHeadlines("",1,5)
        verify(newsObserver, timeout(50)).onChanged(Resource.loading(null).data)
        verify(newsObserver, timeout(50)).onChanged(errorResource.data)
    }


}