package com.learning.careerconnect.Model

data class ResetPasswordIM(
    var email: String? = null, // b@gmail.com
    var otp: String? = null, // 914568
    var password: String? = null // 12345678
)