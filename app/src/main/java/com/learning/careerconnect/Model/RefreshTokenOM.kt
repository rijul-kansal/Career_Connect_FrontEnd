package com.learning.careerconnect.Model

data class RefreshTokenOM(
    var status: String? = null, // success
    var message: String? = null, // success

    var token: String? = null // eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoiYkBnbWFpbC5jb20iLCJpYXQiOjE3Mjc1MTMwMjYsImV4cCI6MTcyODM3NzAyNn0.dTZ6JUUfbMK8rtQc6NEhNcJgtpBjhQP6ubD4AY2l7uw
)