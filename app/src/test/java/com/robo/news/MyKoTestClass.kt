package com.robo.news

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.robo.news.source.network.Resource
import com.robo.news.source.network.Status

import com.robo.news.source.news.NewsModel
import com.robo.news.source.news.NewsRepository
import com.robo.news.ui.home.HomeViewModel

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain




 class MyKoTestClass : DescribeSpec({


     var articles = NewsModel()
     lateinit var viewModel: HomeViewModel
     lateinit var newsRepository: NewsRepository
     lateinit var newsObserver: Observer<Status>

     val successResource = Resource.success(NewsModel("",0, emptyList()))
    val errorResource = Resource.error("Unauthorised", null)

//     @Rule
//     @JvmField
     val instantExecutorRule = InstantTaskExecutorRule()

    // @ExperimentalCoroutinesApi
     val mainThreadSurrogate = newSingleThreadContext("UI thread")


     beforeSpec {
        Dispatchers.setMain(mainThreadSurrogate)
         newsRepository = mock()

         runBlocking {
             whenever(newsRepository.getHeadlines("",1,5)).thenReturn(successResource.data)
             whenever(newsRepository.getHeadlines("",1,5)).thenReturn(errorResource.data)
         }

         viewModel = HomeViewModel(newsRepository)
         newsObserver = mock()

    }

     afterSpec {
         Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
         mainThreadSurrogate.close()
     }

    describe("generateToken") {

            context("when fetch News success") {
                viewModel.status.observeForever(newsObserver)
                viewModel.fetch()
                delay(10)
                verify(newsRepository).getHeadlines("",1,5)
                verify(newsObserver, com.nhaarman.mockitokotlin2.timeout(50)).onChanged(Status.START)
                verify(newsObserver, com.nhaarman.mockitokotlin2.timeout(50)).onChanged(Status.SUCCESS)
                it("verify token") {
                    viewModel.status shouldBe Status.SUCCESS
                }
            }
            context("when fetch News is failed") {
                viewModel.status.observeForever(newsObserver)
                viewModel.fetch()
                delay(10)
                verify(newsRepository).getHeadlines("",1,5)
                verify(newsObserver, com.nhaarman.mockitokotlin2.timeout(50)).onChanged(Status.START)
                verify(newsObserver, com.nhaarman.mockitokotlin2.timeout(50)).onChanged(Status.ERROR)
                it("verify token") {
                    viewModel.status shouldBe Status.ERROR
                }
            }
    }

})




