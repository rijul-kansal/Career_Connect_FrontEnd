package com.learning.careerconnect.Model

data class ResetPasswordOM(
    var message: String? = null, // Please check your email or otp
    var status: String? = null // fail
)