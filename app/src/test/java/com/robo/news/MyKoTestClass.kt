package com.robo.news

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.robo.news.source.network.Resource

import com.robo.news.source.news.NewsModel
import com.robo.news.source.news.NewsRepository
import com.robo.news.ui.home.HomeViewModel
import io.kotest.core.spec.style.AnnotationSpec

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.engine.config.ConfigManager.init
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext

@ExtendWith(InstantExecutorExtension::class)
class MyKoTestClass : DescribeSpec({

      lateinit var viewModel: HomeViewModel
      lateinit var newsRepository: NewsRepository
      lateinit var newsObserver: Observer<Resource<NewsModel>>

      val successResource = Resource.success(NewsModel("Success",0, emptyList()));
      val errorResource = Resource.error("Unauthorised", NewsModel("Error 404",0, emptyList()))

// Create the mock instance once and reset them before or after each test.

     beforeSpec {
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
     }



    describe("fetch News test") {

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

        context("when isActivated is true") {
            val token = viewModel.generateToken()
            it("verify token") {
                token shouldBe "Token"
            }
        }
        context("when isActivated is false") {
            viewModel.isActivated = false
            val token = viewModel.generateToken()
            it("verify token") {
                token shouldBe ""
            }
        }
    }


     })


// setup exucuter

class InstantExecutorExtension : BeforeEachCallback, AfterEachCallback {

    override fun beforeEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance()
            .setDelegate(object : TaskExecutor() {
                override fun executeOnDiskIO(runnable: Runnable) = runnable.run()

                override fun postToMainThread(runnable: Runnable) = runnable.run()

                override fun isMainThread(): Boolean = true
            })
    }

    override fun afterEach(context: ExtensionContext?) {
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}



