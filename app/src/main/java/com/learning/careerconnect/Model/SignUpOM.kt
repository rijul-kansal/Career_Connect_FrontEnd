package com.learning.careerconnect.Model

data class SignUpOM(
    var `data`: Data? = null,
    var status: String? = null,
    var message:String?=null
) {
    data class Data(
        var `data`: Data? = null
    ) {
        data class Data(
            var __v: Int? = null, // 0
            var _id: String? = null, // 66f311fb684aa863a5554d05
            var achievements: List<Any?>? = null,
            var certificateEarned: List<Any?>? = null,
            var codingProfileLink: List<Any?>? = null,
            var education: List<Any?>? = null,
            var email: String? = null, // bb@gmail.com
            var experience: List<Any?>? = null,
            var language: List<Any?>? = null,
            var mobileNumber: Long? = null, // 9876543210
            var name: String? = null, // Recruiter Z
            var project: List<Any?>? = null,
            var skills: List<Any?>? = null,
            var typeOfUser: String? = null // Recruiter
        )
    }
}