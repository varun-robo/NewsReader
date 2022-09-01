package com.robo.news

import com.robo.news.source.network.networkModule
import com.robo.news.source.news.repositoryModule
import com.robo.news.source.persistence.databaseModule
import com.robo.news.ui.bookmark.bookmarkModule
import com.robo.news.ui.bookmark.bookmarkViewModel
import com.robo.news.ui.detail.detailModul
import com.robo.news.ui.detail.detailViewModel
import com.robo.news.ui.home.HomeViewModel
import com.robo.news.ui.home.homeModule
import com.robo.news.ui.home.homeViewModel
//import io.kotest.core.spec.style.DescribeSpec
//import io.kotest.engine.config.ConfigManager.init
//import io.kotest.koin.KoinExtension
//import io.kotest.koin.KoinLifecycleMode
//import io.kotest.koin.KoinListener
//import io.kotest.matchers.shouldBe
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject
import org.koin.test.KoinTest
import kotlin.reflect.KProperty

class TestViewModel() {
    var isActivated = true

    fun generateToken() : String = if (isActivated) "Token" else ""
}

class MyKoTestClass  //DescribeSpec(), KoinTest
    {
//        var  homeNewsModel   by inject<HomeViewModel>()
//        init {
//            test("Make a test with Koin") {
//                startKoin { modules(networkModule,
//                    repositoryModule,
//                    homeViewModel,
//                    homeModule,
//                    bookmarkViewModel,
//                    bookmarkModule,
//                    databaseModule,
//                    detailViewModel,
//                    detailModul) }
//
//                // use componentA here!
//                beforeSpec {
//                    // Lazy inject property
//
//                }
//            }
//
//            describe("generateToken") {
//                context("when isActivated is true") {
//                    val token = homeNewsModel.generateToken()
//                    it("verify token") {
//                        token shouldBe "Token"
//                    }
//                }
//                context("when isActivated is false") {
//                    homeNewsModel.isActivated = false
//                    val token = homeNewsModel.generateToken()
//                    it("verify token") {
//                        token shouldBe ""
//                    }
//                }
//            }
//        }
//
//
//}
//
//private operator fun <T> Lazy<T>.setValue(myKoTestClass: MyKoTestClass, property: KProperty<T?>, t: T) {

}
