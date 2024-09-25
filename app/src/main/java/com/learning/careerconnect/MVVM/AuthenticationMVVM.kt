package com.learning.careerconnect.MVVM

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.careerconnect.Activity.SignInActivity
import com.learning.careerconnect.Activity.SignUpActivity
import com.learning.careerconnect.Model.LoginIM
import com.learning.careerconnect.Model.LoginOM
import com.learning.careerconnect.Model.ResendOTPIM
import com.learning.careerconnect.Model.ResendOTPOM
import com.learning.careerconnect.Model.SignUpIM
import com.learning.careerconnect.Model.SignUpOM
import com.learning.careerconnect.Model.VerifyOTPIM
import com.learning.careerconnect.Model.VerifyOTPOM
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.Utils.Retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationMVVM : ViewModel() {

    // Sign up user
    var resultOfSignUpUser: MutableLiveData<SignUpOM> = MutableLiveData()
    fun signUpUser(input: SignUpIM, context: Context, activity : SignUpActivity) {
        try {
            if (Constants.checkForInternet(context)) {
                val func = Constants.getInstance().create(Retrofit::class.java)
                viewModelScope.launch {
                    val result = func.signUp(input)
//                    Log.d("rk",result.toString())
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfSignUpUser.value = result.body()
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = Constants.parseErrorMessage(errorBody)
                            activity.errorFn(errorMessage ?: "Unknown error",context)
                        }
                    }
                }
            } else {
                activity.errorFn("No internet connection",context)
            }
        } catch (err: Exception) {
            Log.e("rk", "Exception occurred during sign up: ${err.message}")
        }
    }
    fun observerForSignUpUser(): LiveData<SignUpOM> = resultOfSignUpUser


    // verify email
    var resultOfVerifyOTPUser: MutableLiveData<VerifyOTPOM> = MutableLiveData()
    fun verifyOTP(input: VerifyOTPIM, context: Context, activity : Activity) {
        try {
            if (Constants.checkForInternet(context)) {
                val func = Constants.getInstance().create(Retrofit::class.java)
                viewModelScope.launch {
                    val result = func.verifyOTP(input)
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfVerifyOTPUser.value = result.body()
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = Constants.parseErrorMessage(errorBody)
                            when(activity)
                            {
                                is SignUpActivity->{
                                    activity.errorFnForVerifyOTP(errorMessage ?: "Unknown error",context)
                                }
//                                is SignInActivity ->{
//                                    activity.errorFnForVerifyOTP(errorMessage ?: "Unknown error")
//                                }
                            }
                        }
                    }
                }
            } else {
                when(activity)
                {
                    is SignUpActivity->{
                        activity.errorFnForVerifyOTP("No internet connection",context)
                    }
//                    is SignInActivity ->{
//                        activity.errorFnForVerifyOTP("No internet connection")
//                    }
                }
            }
        } catch (err: Exception) {
            Log.e("rk", "Exception occurred during sign up: ${err.message}")
        }
    }
    fun observerForVerifyOTPUser(): LiveData<VerifyOTPOM> = resultOfVerifyOTPUser

    // resend verification code
    var resultOfResendOTP: MutableLiveData<ResendOTPOM> = MutableLiveData()
    fun resendOTP(input: ResendOTPIM, context: Context, activity : Activity) {
        try {
            if (Constants.checkForInternet(context)) {
                val func = Constants.getInstance().create(Retrofit::class.java)
                viewModelScope.launch {
                    val result = func.resendOTP(input)
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfResendOTP.value = result.body()
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = Constants.parseErrorMessage(errorBody)

                            when(activity)
                            {
                                is SignUpActivity->{
                                    activity.errorFn(errorMessage ?: "Unknown error",context)
                                }
//                                is SignInActivity ->{
//                                    activity.errorFn(errorMessage ?: "Unknown error")
//                                }
                            }

                        }
                    }
                }
            } else {
                when(activity)
                {
                    is SignUpActivity->{
                        activity.errorFn("No internet connection",context)
                    }
//                    is SignInActivity ->{
//                        activity.errorFn("No internet connection")
//                    }
                }
            }
        } catch (err: Exception) {
            Log.e("rk", "Exception occurred during sign up: ${err.message}")
        }
    }
    fun observerForResendOTP(): LiveData<ResendOTPOM> = resultOfResendOTP


    // login User
    var resultOfLoginUser: MutableLiveData<LoginOM> = MutableLiveData()
    fun loginUser(input: LoginIM, context: Context, activity : SignInActivity) {
        try {
            if (Constants.checkForInternet(context)) {
                val func = Constants.getInstance().create(Retrofit::class.java)
                viewModelScope.launch {
                    val result = func.login(input)
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfLoginUser.value = result.body()
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = Constants.parseErrorMessage(errorBody)
                            activity.errorFn(errorMessage ?: "Unknown error")

                        }
                    }
                }
            } else {
                activity.errorFn("No internet connection")
            }
        } catch (err: Exception) {
            Log.e("rk", "Exception occurred during sign up: ${err.message}")
        }
    }
    fun observerForLoginUser(): LiveData<LoginOM> = resultOfLoginUser

}