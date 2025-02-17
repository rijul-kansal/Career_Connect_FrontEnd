package com.learning.careerconnect.MVVM

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.careerconnect.Activity.DisplayJobInDetailActivity
import com.learning.careerconnect.Activity.MainActivity
import com.learning.careerconnect.Model.AllSavedJobsJobIdOnlyOM
import com.learning.careerconnect.Model.ApplyJobIM
import com.learning.careerconnect.Model.ApplyJobOM
import com.learning.careerconnect.Model.GetAllAppliedJobsOM
import com.learning.careerconnect.Model.GetAllSavedLaterJobsOM
import com.learning.careerconnect.Model.GetAllTypeOfInformationOM
import com.learning.careerconnect.Model.SavedJobIM
import com.learning.careerconnect.Model.SavedJobOM
import com.learning.careerconnect.Model.SearchAllJobsOM
import com.learning.careerconnect.Model.UnSavedJobIM
import com.learning.careerconnect.Model.UnSavedJobOM
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.Utils.Retrofit
import com.learning.careerconnect.fragment.AppliedJobFragment
import com.learning.careerconnect.fragment.SavedJobFragment
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

    // get all saved  jobs only job id
    var resultOfGetAllSavedJobOnlyJobId: MutableLiveData<AllSavedJobsJobIdOnlyOM> = MutableLiveData()
    fun getAllSavedJobOnlyJobId(activity:MainActivity,token:String) {
        try {
            if (Constants.checkForInternet(activity)) {
                val func = Constants.getInstance().create(Retrofit::class.java)
                viewModelScope.launch {
                    val result = func.getAllSavedJobJobIdOnly(token)
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfGetAllSavedJobOnlyJobId.value = result.body()
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
    fun observerForGetAllSavedJobOnlyJobId(): LiveData<AllSavedJobsJobIdOnlyOM> = resultOfGetAllSavedJobOnlyJobId

    // unsave job for later
    var resultOfUnSaveJob: MutableLiveData<UnSavedJobOM> = MutableLiveData()
    fun unSaveJob(activity:DisplayJobInDetailActivity,token:String, input:UnSavedJobIM) {
        try {
            if (Constants.checkForInternet(activity)) {
                val func = Constants.getInstance().create(Retrofit::class.java)
                viewModelScope.launch {
                    val result = func.unSaveJob(token,input)
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfUnSaveJob.value = result.body()
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
    fun observerForUnSaveJob(): LiveData<UnSavedJobOM> = resultOfUnSaveJob


    // unsave job for later
    var resultOfGetAllAppliedJobs: MutableLiveData<GetAllAppliedJobsOM> = MutableLiveData()
    fun getAllAppliedJobs(context: Context,token:String , fragment:AppliedJobFragment,skip:String) {
        try {
            if (Constants.checkForInternet(context)) {
                val func = Constants.getInstance().create(Retrofit::class.java)
                viewModelScope.launch {
                    val result = func.getAllAppliedJobs(token,skip,"10")
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfGetAllAppliedJobs.value = result.body()
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
    fun observerForGetAllAppliedJobs(): LiveData<GetAllAppliedJobsOM> = resultOfGetAllAppliedJobs


    // get all saved later jobs full data
    var resultOfGetAllSavedLaterJobsFull: MutableLiveData<GetAllSavedLaterJobsOM> = MutableLiveData()
    fun getAllSavedLaterJobsFull(context:Context,activity: MainActivity?, token:String, fragment: Fragment, skip:String) {
        try {
            if (Constants.checkForInternet(context)) {
                val func = Constants.getInstance().create(Retrofit::class.java)
                viewModelScope.launch {
                    val result = func.getAllSavedLaterJobs(token,skip,"10")
                    withContext(Dispatchers.Main) {
                        if (result.isSuccessful) {
                            resultOfGetAllSavedLaterJobsFull.value = result.body()
                        } else {
                            val errorBody = result.errorBody()?.string()
                            val errorMessage = Constants.parseErrorMessage(errorBody)
                            when(fragment)
                            {
                                is SavedJobFragment -> fragment.errorFn(errorMessage ?: "Unknown error")
                                is AppliedJobFragment -> fragment.errorFn(errorMessage ?: "Unknown error")
                            }
                            activity?.errorFn(errorMessage ?: "Unknown error")
                        }
                    }
                }
            } else {
                when(fragment)
                {
                    is SavedJobFragment -> fragment.errorFn("No internet connection")
                    is AppliedJobFragment -> fragment.errorFn("No internet connection")
                }
                activity?.errorFn("No internet connection")
            }
        } catch (err: Exception) {
            Log.e("rk", "Exception occurred during sign up: ${err.message}")
        }
    }
    fun observerForGetAllSavedLaterJobsFull(): LiveData<GetAllSavedLaterJobsOM> = resultOfGetAllSavedLaterJobsFull
}