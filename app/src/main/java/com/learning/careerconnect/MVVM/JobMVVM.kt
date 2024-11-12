package com.learning.careerconnect.MVVM

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.careerconnect.Activity.DisplayJobInDetailActivity
import com.learning.careerconnect.Model.ApplyJobIM
import com.learning.careerconnect.Model.ApplyJobOM
import com.learning.careerconnect.Model.GetAllTypeOfInformationOM
import com.learning.careerconnect.Model.SavedJobIM
import com.learning.careerconnect.Model.SavedJobOM
import com.learning.careerconnect.Model.SearchAllJobsOM
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.Utils.Retrofit
import com.learning.careerconnect.fragment.SearchJobFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JobMVVM : ViewModel() {

    // search job
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

    // get all info about jobs
    var resultOfgetAllTypeOfInfo: MutableLiveData<GetAllTypeOfInformationOM> = MutableLiveData()
    fun getAllTypeOfInfo(fragment:SearchJobFragment,token:String,context: Context) {
        try {
            if (Constants.checkForInternet(context)) {
                val func = Constants.getInstance().create(Retrofit::class.java)
                viewModelScope.launch {
                    val result = func.getAllTypeOfInfo(token)
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfgetAllTypeOfInfo.value = result.body()
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
    fun observerForgetAllTypeOfInfo(): LiveData<GetAllTypeOfInformationOM> = resultOfgetAllTypeOfInfo

    // apply for jobs
    var resultOfApplyForJob: MutableLiveData<ApplyJobOM> = MutableLiveData()
    fun applyForJob(activity:DisplayJobInDetailActivity,token:String,input:ApplyJobIM) {
        try {
            if (Constants.checkForInternet(activity)) {
                val func = Constants.getInstance().create(Retrofit::class.java)
                viewModelScope.launch {
                    val result = func.applyForJob(token,input)
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfApplyForJob.value = result.body()
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
    fun observerForApplyForJob(): LiveData<ApplyJobOM> = resultOfApplyForJob


    // save  jobs for later
    var resultOfSaveJobForLater: MutableLiveData<SavedJobOM> = MutableLiveData()
    fun saveJobForLater(activity:DisplayJobInDetailActivity,token:String,input:SavedJobIM) {
        try {
            if (Constants.checkForInternet(activity)) {
                val func = Constants.getInstance().create(Retrofit::class.java)
                viewModelScope.launch {
                    val result = func.saveLaterJob(token,input)
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfSaveJobForLater.value = result.body()
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
    fun observerForSaveJobForLater(): LiveData<SavedJobOM> = resultOfSaveJobForLater
}