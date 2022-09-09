package com.robo.news.source.network

data class Resource<out T>(val status: Status, val news: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }


        fun <T> error(msg: String, news: T?): Resource<T> {
            return Resource(Status.ERROR, news, msg)
        }

        fun <T> loading(news: T?): Resource<T> {
            return Resource(Status.LOADING, news, null)
        }
    }
}
