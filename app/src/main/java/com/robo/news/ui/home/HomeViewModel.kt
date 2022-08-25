package com.robo.news.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.robo.news.source.news.NewsModel
import com.robo.news.source.news.NewsRepository
import kotlinx.coroutines.launch
import org.koin.dsl.module
import kotlin.math.ceil

val homeViewModel = module {
    factory { HomeViewModel(get()) }
}

class HomeViewModel(
    val repository: NewsRepository
) : ViewModel() {
    val title = ""

  //  val category by lazy { MutableLiveData<String>() }
    val message by lazy { MutableLiveData<String>() }
    val loading by lazy { MutableLiveData<Boolean>() }
    val loadMore by lazy { MutableLiveData<Boolean>() }
    val news by lazy { MutableLiveData<NewsModel>() }

    init {
      //  category.value = ""
        message.value = null
    }

    var query = ""
    var page = 1
    var maxpage = 4
    var pageSize = 5

    fun fetch() {
        loading.value = true
        if (page > 1) loadMore.value = true else loading.value = true
        viewModelScope.launch {
            try {
                val response = repository.fetch( query, page,pageSize)
                news.value = response
                val totalResult : Double = (response.totalResults / pageSize).toDouble()
                maxpage = ceil(totalResult).toInt()
                page++
                loading.value = false
                loadMore.value = false
            } catch (e: Exception) {
                message.value = e.message
            }
        }
    }


}