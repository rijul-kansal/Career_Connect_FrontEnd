package com.learning.careerconnect.Utils

import com.learning.careerconnect.Model.LoginIM
import com.learning.careerconnect.Model.LoginOM
import com.learning.careerconnect.Model.ResendOTPIM
import com.learning.careerconnect.Model.ResendOTPOM
import com.learning.careerconnect.Model.ResetPasswordIM
import com.learning.careerconnect.Model.ResetPasswordOM
import com.learning.careerconnect.Model.SignUpIM
import com.learning.careerconnect.Model.SignUpOM
import com.learning.careerconnect.Model.UpdateMeIM
//import com.learning.careerconnect.Model.UpdateMeIM
import com.learning.careerconnect.Model.UpdateMeOM
import com.learning.careerconnect.Model.VerifyOTPIM
import com.learning.careerconnect.Model.VerifyOTPOM
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface Retrofit {
    @POST("/v1/authentication/signUp")
    suspend fun signUp(@Body body : SignUpIM) : Response<SignUpOM>
    @POST("/v1/authentication/verifyOTP")
    suspend fun verifyOTP(@Body body : VerifyOTPIM) : Response<VerifyOTPOM>
    @PATCH("/v1/authentication/resendOTPOrForgottenPassword")
    suspend fun resendOTP(@Body body : ResendOTPIM) : Response<ResendOTPOM>

    @POST("/v1/authentication/login")
    suspend fun login(@Body body : LoginIM) : Response<LoginOM>
    @PATCH("/v1/authentication/resetPassword")
    suspend fun resetPassword(@Body body : ResetPasswordIM) : Response<ResetPasswordOM>

    @PATCH("/v1/user")
    suspend fun updateMe(@Header("authorization") authHeader:String, @Body body : UpdateMeIM) : Response<UpdateMeOM>
}