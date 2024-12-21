package com.learning.careerconnect.MVVM

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.careerconnect.Activity.MainActivity
import com.learning.careerconnect.Model.GetQuizScoreEarnedOM
import com.learning.careerconnect.Model.GetQuizTypeOM
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.Utils.Retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuizMVVM : ViewModel() {

    var resultOfGetQuizType: MutableLiveData<GetQuizTypeOM> = MutableLiveData()
    fun getQuizType(activity:MainActivity, token:String,) {
    try {
            if (Constants.checkForInternet(activity)) {
                val func = Constants.getInstance().create(Retrofit::class.java)
                viewModelScope.launch {
                    val result = func.getQuizTypes(token)
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfGetQuizType.value = result.body()
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
    fun observerForgetQuizType(): LiveData<GetQuizTypeOM> = resultOfGetQuizType

    var resultOfGetScoreEarned: MutableLiveData<GetQuizScoreEarnedOM> = MutableLiveData()
    fun getScoreEarned(activity:MainActivity, token:String,) {
        try {
            if (Constants.checkForInternet(activity)) {
                val func = Constants.getInstance().create(Retrofit::class.java)
                viewModelScope.launch {
                    val result = func.getQuizScores(token)
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfGetScoreEarned.value = result.body()
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
    fun observerForGetScoreEarned(): LiveData<GetQuizScoreEarnedOM> = resultOfGetScoreEarned
}