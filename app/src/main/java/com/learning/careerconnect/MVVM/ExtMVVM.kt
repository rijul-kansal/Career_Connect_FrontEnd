package com.learning.careerconnect.MVVM

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learning.careerconnect.Model.SingleCityIM
import com.learning.careerconnect.Model.SingleCityOM
import com.learning.careerconnect.Model.SingleStateIM
import com.learning.careerconnect.Model.SingleStateOM
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.Utils.Retrofit
import com.learning.careerconnect.fragment.Profile.PersonalInfoFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExtMVVM : ViewModel() {
    var resultOfGetSingleState: MutableLiveData<SingleStateOM> = MutableLiveData()
    fun getSingleState(fragment: PersonalInfoFragment, context:Context, input: SingleStateIM) {
        try {
            if (Constants.checkForInternet(context)) {
                val func = Constants.getInstance().create(Retrofit::class.java)
                viewModelScope.launch {
                    val result = func.getSingleState(input)
                    withContext(Dispatchers.Main) {

                        if (result.isSuccessful) {
                            resultOfGetSingleState.value = result.body()
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
    fun observerForGetSingleStateSearch(): LiveData<SingleStateOM> = resultOfGetSingleState


    var resultOfGetSingleCity: MutableLiveData<SingleCityOM> = MutableLiveData()
    fun getSingleCity(fragment: PersonalInfoFragment, context:Context, input: SingleCityIM) {
        try {
            if (Constants.checkForInternet(context)) {
                val func = Constants.getInstance().create(Retrofit::class.java)
                viewModelScope.launch {
                    val result = func.getSingleCity(input)
                    withContext(Dispatchers.Main) {

                        if (result.isSuccessful) {
                            resultOfGetSingleCity.value = result.body()
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
    fun observerForGetSingleCitySearch(): LiveData<SingleCityOM> = resultOfGetSingleCity
}