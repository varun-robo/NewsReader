package com.news.newsApp.sample


import com.robo.news.source.news.ArticleModel

import com.robo.news.source.news.SourceModel

val dummyArticleTest = ArticleModel(
    SourceModel(
        "someId",
        "Some Name"
    ),"JohnDoe",
    "Some content",
    "Some Desc",
    "2021-06-12T01:53:53Z",
    "Some title",
    "Some url",
    "some other url"

)

val dummyArticlesTest = arrayListOf(1, 2, 3, 4).map {
    dummyArticleTest.copy(title = "Title $it")
}