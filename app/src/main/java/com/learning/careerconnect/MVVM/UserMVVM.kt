package com.learning.careerconnect.MVVM

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.careerconnect.Activity.SignInActivity
import com.learning.careerconnect.Model.UpdateMeIM
import com.learning.careerconnect.Model.UpdateMeOM
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.Utils.Retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserMVVM : ViewModel() {

    var resultOfUpdateMe: MutableLiveData<UpdateMeOM> = MutableLiveData()
    fun updateMe(input: UpdateMeIM, context: Context, activity : SignInActivity,token:String) {
        try {
            if (Constants.checkForInternet(context)) {
                val func = Constants.getInstance().create(Retrofit::class.java)
                viewModelScope.launch {
                    val result = func.updateMe(token,input)
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            Log.d("rk",result.toString())
                            resultOfUpdateMe.value = result.body()
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
    fun observerForUpdateMe(): LiveData<UpdateMeOM> = resultOfUpdateMe
}