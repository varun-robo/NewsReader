package com.robo.news

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.robo.news.source.network.Resource

import com.robo.news.source.news.NewsModel
import com.robo.news.source.news.NewsRepository
import com.robo.news.ui.home.HomeViewModel

import io.kotest.core.spec.style.DescribeSpec
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain




 class MyKoTestClass : DescribeSpec({


      lateinit var viewModel: HomeViewModel
      lateinit var newsRepository: NewsRepository
      lateinit var newsObserver: Observer<Resource<NewsModel>>

      val successResource = Resource.success(NewsModel("Success",0, emptyList()));
      val errorResource = Resource.error("Unauthorised", NewsModel("Error 404",0, emptyList()))

//     @Rule
//     @JvmField
     val instantExecutorRule = InstantTaskExecutorRule()

    // @ExperimentalCoroutinesApi
     val mainThreadSurrogate = newSingleThreadContext("UI thread")


     beforeSpec {
        Dispatchers.setMain(mainThreadSurrogate)
         newsRepository = mock()

         runBlocking {
             whenever(newsRepository.getHeadlines("Success",1,5)).thenReturn(successResource)
             whenever(newsRepository.getHeadlines("Error",1,5)).thenReturn(errorResource)
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
                viewModel.news.observeForever(newsObserver)
                viewModel.repository.getHeadlines("Success",1,5)
                delay(10)
                verify(newsRepository).getHeadlines("Success",1,5)
                verify(newsObserver, com.nhaarman.mockitokotlin2.timeout(50)).onChanged(Resource.loading(null))
                verify(newsObserver, com.nhaarman.mockitokotlin2.timeout(50)).onChanged(successResource)
            }
            context("when fetch News is failed") {
                viewModel.news.observeForever(newsObserver)
                viewModel.repository.getHeadlines("Error",1,5)
                delay(10)
                verify(newsRepository).getHeadlines("Error",1,5)
                verify(newsObserver, com.nhaarman.mockitokotlin2.timeout(50)).onChanged(Resource.loading(null))
                verify(newsObserver, com.nhaarman.mockitokotlin2.timeout(50)).onChanged(errorResource)
            }
    }
})




