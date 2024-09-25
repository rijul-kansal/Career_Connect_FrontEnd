package com.learning.careerconnect.Model

data class ResendOTPOM(
    var message: String? = null, // User with this email does not exist
    var status: String? = null // fail
)