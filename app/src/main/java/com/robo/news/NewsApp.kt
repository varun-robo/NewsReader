package com.robo.news

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.robo.news.source.network.networkModule
import com.robo.news.source.news.repositoryModule
import com.robo.news.source.persistence.databaseModule
import com.robo.news.ui.bookmark.bookmarkModule
import com.robo.news.ui.bookmark.bookmarkViewModel
import com.robo.news.ui.detail.detailModul
import com.robo.news.ui.detail.detailViewModel
import com.robo.news.ui.home.homeModule
import com.robo.news.ui.home.homeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class NewsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.e("run base application")
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        startKoin {
            androidLogger()
            androidContext(this@NewsApp)
            modules(
                networkModule,
                repositoryModule,
                homeViewModel,
                homeModule,
                bookmarkViewModel,
                bookmarkModule,
                databaseModule,
                detailViewModel,
                detailModul

            )
        }
    }
}