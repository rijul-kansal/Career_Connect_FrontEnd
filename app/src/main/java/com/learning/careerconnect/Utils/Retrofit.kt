package com.learning.careerconnect.Utils

import com.learning.careerconnect.Model.AllSavedJobsJobIdOnlyOM
import com.learning.careerconnect.Model.ApplyJobIM
import com.learning.careerconnect.Model.ApplyJobOM
import com.learning.careerconnect.Model.GetAllTypeOfInformationOM
import com.learning.careerconnect.Model.LoginIM
import com.learning.careerconnect.Model.LoginOM
import com.learning.careerconnect.Model.RefreshTokenIM
import com.learning.careerconnect.Model.RefreshTokenOM
import com.learning.careerconnect.Model.ResendOTPIM
import com.learning.careerconnect.Model.ResendOTPOM
import com.learning.careerconnect.Model.ResetPasswordIM
import com.learning.careerconnect.Model.ResetPasswordOM
import com.learning.careerconnect.Model.SavedJobIM
import com.learning.careerconnect.Model.SavedJobOM
import com.learning.careerconnect.Model.SearchAllJobsOM
import com.learning.careerconnect.Model.SignUpIM
import com.learning.careerconnect.Model.SignUpOM
import com.learning.careerconnect.Model.UnSavedJobIM
import com.learning.careerconnect.Model.UnSavedJobOM
import com.learning.careerconnect.Model.UpdateMeIM
import com.learning.careerconnect.Model.UpdateMeOM
import com.learning.careerconnect.Model.VerifyOTPIM
import com.learning.careerconnect.Model.VerifyOTPOM
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

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
    @POST("/v1/authentication/refreshToken")
    suspend fun refreshToken(@Header("authorization") authHeader:String, @Body body : RefreshTokenIM) : Response<RefreshTokenOM>
    @GET("/v1/jobs/searchJobs")
    suspend fun searchAllJobs(
        @Header("authorization") authHeader:String,
        @Query("preferredJobType") preferredJobType: String? = null,
        @Query("skip") skip: String? = null,
        @Query("limit") limit: String? = null,
        @Query("typeOfJob") typeOfJob: String? = null,
        @Query("location") location: String? = null,
        @Query("skill") skill: String? = null,
        @Query("companyNames") companyNames: String? = null,
        @Query("easyApply") easyApply: String? = null,
        @Query("time") time: String? = null) : Response<SearchAllJobsOM>
    @GET("/v1/jobs/getAllJobTypes")
    suspend fun getAllTypeOfInfo(
        @Header("authorization") authHeader:String) : Response<GetAllTypeOfInformationOM>
    @POST("/v1/jobs/apply")
    suspend fun applyForJob(
        @Header("authorization") authHeader:String, @Body body : ApplyJobIM) : Response<ApplyJobOM>
    @POST("/v1/jobs/saveLater")
    suspend fun saveLaterJob(
        @Header("authorization") authHeader:String, @Body body : SavedJobIM) : Response<SavedJobOM>
    @POST("/v1/jobs/unSaveLater")
    suspend fun unSaveJob(
        @Header("authorization") authHeader:String, @Body body : UnSavedJobIM) : Response<UnSavedJobOM>
    @GET("/v1/jobs/getAllSavedLaterJobsJobIdOnly")
    suspend fun getAllSavedJobJobIdOnly(
        @Header("authorization") authHeader:String) : Response<AllSavedJobsJobIdOnlyOM>
}