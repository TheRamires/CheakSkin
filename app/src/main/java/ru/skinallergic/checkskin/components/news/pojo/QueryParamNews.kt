package ru.skinallergic.checkskin.components.news.pojo

data class QueryParamNews (
        var word: String,
        var keywords:String,
        var page:Int,
        var count:Int
        )