package com.learning.careerconnect.Model

data class GetAllTypeOfInformationOM(
    var `data`: Data? = null,
    var status: String? = null // success
) {
    data class Data(
        var role: List<String?>? = null,
        var skill: List<String?>? = null
    )
}