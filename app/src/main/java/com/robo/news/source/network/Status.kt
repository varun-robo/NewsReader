package com.robo.news.source.network

sealed class Status(val status:Int) {
    object SUCCESS : Status(1)
    object ERROR   : Status(2)
    object LOADING  : Status(3)
}