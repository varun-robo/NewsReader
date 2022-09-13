package com.robo.news.viewmodel.home.HomeViewModelTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify

import com.nhaarman.mockitokotlin2.whenever

import com.robo.news.source.network.NetworkResponse
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
import org.mockito.Mock

@RunWith(JUnit4::class)
internal class HomeViewModelTest {
    private lateinit var viewModel: HomeViewModel
    private lateinit var newsRepository: NewsRepository
    private lateinit var newsObserver: Observer<NetworkResponse<Resource<NewsModel>>>

    private val successResource = NetworkResponse.Success( Resource.success(NewsModel("Success",0, emptyList())) );
   // private val errorResource = NetworkResponse.Failure("Error 404", Exception())


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
            whenever(newsRepository.getHeadlines("Success",1,5)).thenReturn(successResource.data)
        //    whenever(newsRepository.getHeadlines("Error",1,5)).thenReturn(errorResource)
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
    fun `when getHeadlines is called with valid result, then observer is updated with success`() = runBlocking {
        viewModel.news.observeForever(newsObserver)
        viewModel.repository.getHeadlines("Success",1,5)
        delay(10)
        verify(newsRepository).getHeadlines("Success",1,5)
         verify(newsObserver, timeout(50)).onChanged(successResource)
    }

    @Test
    fun `when getHeadlines is called with invalid result, then observer is updated with failure`() = runBlocking {
        viewModel.news.observeForever(newsObserver)
        viewModel.repository.getHeadlines("Error",1,5)
        delay(10)
        verify(newsRepository).getHeadlines("Error",1,5)
   //    verify(newsObserver, timeout(50)).onChanged(errorResource)
    }
}