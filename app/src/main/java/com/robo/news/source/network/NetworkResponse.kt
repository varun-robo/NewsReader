package com.robo.news.source.network


sealed class NetworkResponse<T>(
    val data : T? = null,
    val error : String? = null,
    val execption : Exception? = null
){
    class Success<T> (data : T) : NetworkResponse<T>(data = data)
    class Failure<T> (error : String, execption : Exception) : NetworkResponse<T>(error = error , execption = execption)
    class Loading<T> () : NetworkResponse<T>()
}