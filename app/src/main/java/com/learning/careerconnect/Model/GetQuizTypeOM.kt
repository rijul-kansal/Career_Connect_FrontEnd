package com.learning.careerconnect.Model


data class GetQuizTypeOM(
    var `data`: Data?,
    var status: String? ,
    var message: String?

) {
    data class Data(
        var `data`: List<String?>?
    )
}