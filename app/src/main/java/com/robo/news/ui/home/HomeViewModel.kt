package com.robo.news.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robo.news.source.network.NetworkResponse
import com.robo.news.source.network.Resource
import com.robo.news.source.network.Status
import com.robo.news.source.news.NewsApiRepositoryInterface

import com.robo.news.source.news.NewsModel
import com.robo.news.source.news.NewsRepository
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import kotlin.math.ceil

val homeViewModel = module {
    viewModel { HomeViewModel(get<NewsRepository>()) }
}

class HomeViewModel(
    val repository: NewsApiRepositoryInterface
) : ViewModel() {
    val title = ""
    var isActivated = true
    val message by lazy { MutableLiveData<String>() }
 //   val status by lazy { MutableLiveData<Status>() }
    val loading by lazy { MutableLiveData<Boolean>() }
    val loadMore by lazy { MutableLiveData<Boolean>() }
    val news by lazy { MutableLiveData<NetworkResponse<Resource<NewsModel>>>() }
  //  private val mErrorMessage = MutableLiveData<String?>()

    init {
      //  category.value = ""
        message.value = null
  //      status.value = Status.LOADING
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
                news.value = NetworkResponse.Loading()
        //        status.value = Status.LOADING
                val response =  repository.getHeadlines( query, page,pageSize)
                news.value = response?.let { NetworkResponse.Success(it) }
         //       status.value = Status.SUCCESS
                val totalResult : Double = (response?.news!!.totalResults / pageSize).toDouble()
                maxpage = ceil(totalResult).toInt()
                page++
                loading.value = false
                loadMore.value = false
            } catch (e: Exception) {
                message.value = e.message
                news.value = message?.let { it.value?.let { it1 -> NetworkResponse.Failure(it1,e) } }
         //       status.value = Status.ERROR
            }
        }
    }

    fun generateToken() : String = if (isActivated) "Token" else ""

}