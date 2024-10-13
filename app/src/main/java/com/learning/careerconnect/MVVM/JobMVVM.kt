package com.learning.careerconnect.MVVM

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.careerconnect.Model.SearchAllJobsOM
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.Utils.Retrofit
import com.learning.careerconnect.fragment.SearchJobFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JobMVVM : ViewModel() {
    var resultOfSearchAllJobs: MutableLiveData<SearchAllJobsOM> = MutableLiveData()
    fun searchAllJobs(fragment:SearchJobFragment,token:String,context: Context,preferredJobType:String?,skip:String?,limit:String?,typeOfJob:String?,location:String?,skill:String?,companyNames:String?,easyApply:String?,time:String?) {
        try {
            if (Constants.checkForInternet(context)) {
                val func = Constants.getInstance().create(Retrofit::class.java)
                viewModelScope.launch {
                    val result = func.searchAllJobs(token ,preferredJobType,skip,limit,typeOfJob,location,skill,companyNames,easyApply,time)
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfSearchAllJobs.value = result.body()
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = Constants.parseErrorMessage(errorBody)
                            fragment.errorFn(errorMessage ?: "Unknown error")
                        }
                    }
                }
            } else {
                fragment.errorFn("No internet connection")
            }
        } catch (err: Exception) {
            Log.e("rk", "Exception occurred during sign up: ${err.message}")
        }
    }
    fun observerForSearchAllJobs(): LiveData<SearchAllJobsOM> = resultOfSearchAllJobs
}