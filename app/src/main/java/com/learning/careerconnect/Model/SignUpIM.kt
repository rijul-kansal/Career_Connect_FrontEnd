package com.learning.careerconnect.Model

data class SignUpIM(
    var email: String? = null, // b@gmail.com
    var mobileNumber: String? = null, // 9876543210
    var name: String? = null, // Recruiter Z
    var password: String? = null, // 12345678
    var typeOfUser: String? = null // Recruiter
)