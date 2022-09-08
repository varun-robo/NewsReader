package com.robo.news

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.robo.news.source.network.Resource

import com.robo.news.source.news.NewsModel
import com.robo.news.source.news.NewsRepository
import com.robo.news.ui.home.HomeViewModel

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
    lateinit var testViewModel: HomeViewModel

     var articles = NewsModel()
     lateinit var viewModel: HomeViewModel
     lateinit var newsRepository: NewsRepository
     lateinit var newsObserver: Observer<NewsModel>

     val successResource = Resource.success(NewsModel("",0, emptyList()))
    val errorResource = Resource.error("Unauthorised", null)

//     @Rule
//     @JvmField
  //   val instantExecutorRule = InstantTaskExecutorRule()

    // @ExperimentalCoroutinesApi
 //    val mainThreadSurrogate = newSingleThreadContext("UI thread")

    init{
        listener(InstantExecutorListener())
    }
     beforeSpec {
   //     Dispatchers.setMain(mainThreadSurrogate)
         newsRepository = mock()

         runBlocking {
             whenever(newsRepository.getHeadlines("",1,5)).thenReturn(successResource.data)
             whenever(newsRepository.getHeadlines("",1,5)).thenReturn(errorResource.data)
         }
         testViewModel = HomeViewModel(newsRepository)

         viewModel = HomeViewModel(newsRepository)
         newsObserver = mock()

    }

     afterSpec {
         Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
         mainThreadSurrogate.close()
     }

    describe("generateToken") {
        context("when isActivated is true") {
            val token = testViewModel.generateToken()
            it("verify token") {
                token shouldBe "Token"
            }
        }
        context("when isActivated is false") {
            testViewModel.isActivated = false
            val token = testViewModel.generateToken()
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



