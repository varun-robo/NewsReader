package com.news.newsApp.di

import com.news.newsApp.repository.FakeNewsApiRepository

import com.robo.news.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val fakeViewModelModule = module {
    viewModel { HomeViewModel(get<FakeNewsApiRepository>()) }
}

val fakeRepositoryModule = module {
    single { FakeNewsApiRepository() }
}