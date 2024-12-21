package com.learning.careerconnect.Model

import java.io.Serializable

data class GetQuizScoreEarnedOM(
    var `data`: Data?,
    var status: String? // success
) :Serializable{
    data class Data(
        var `data`: List<Data?>?
    ):Serializable {
        data class Data(
            var _id: String?, // 6765ac017fd4e837c18f0485
            var date: Long?, // 1734716417489
            var score: Int?, // 10
            var type: String? // Java
        ):Serializable
    }
}