package com.learning.careerconnect.Model

data class VerifyOTPIM(
    var email: String? = null, // b@gmail.com
    var otp: String? = null // 12345678
)