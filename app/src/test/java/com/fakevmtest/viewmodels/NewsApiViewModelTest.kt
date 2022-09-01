package com.news.newsApp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.news.newsApp.di.fakeRepositoryModule
import com.news.newsApp.di.fakeViewModelModule
import com.news.newsApp.getOrAwaitValueTest
import com.news.newsApp.repository.FakeNewsApiRepository
import com.news.newsApp.sample.dummyArticlesTest
import com.google.common.truth.Truth.assertThat
import com.robo.news.source.news.NewsModel
import com.robo.news.ui.home.HomeViewModel
import com.robo.news.util.GENERAL_ERROR
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.koin.core.logger.Level
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

@ExperimentalCoroutinesApi
class NewsApiVMTest: KoinTest {
    private val viewModel by inject<HomeViewModel>()
    private val repository by inject<FakeNewsApiRepository>()

    @get:Rule
    val setupViewModel = KoinTestRule.create {
        printLogger(Level.DEBUG)
        modules(fakeViewModelModule, fakeRepositoryModule)
    }

    @get:Rule
    var executionRule = InstantTaskExecutorRule()

    @Test
    fun `network error populates errorMessage`() = runBlockingTest {
        repository.setNetworkErrorHappened(true)
        viewModel.fetch()
        val err = viewModel.message.getOrAwaitValueTest()
        assertThat(err).isEqualTo(GENERAL_ERROR)
    }

    @Test
    fun `network success populates newsData`() = runBlockingTest {
        repository.setNetworkErrorHappened(false)
        viewModel.fetch()
        val err = viewModel.news.getOrAwaitValueTest()

        val newsData = NewsModel("Success" , 5 , dummyArticlesTest)
        assertThat(err).isEqualTo(newsData)
    }
}