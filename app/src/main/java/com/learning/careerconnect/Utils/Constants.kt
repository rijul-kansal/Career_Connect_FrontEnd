package com.learning.careerconnect.Utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Constants {

    const val EMAIL="email"
    const val PASSWORD="password"
    const val GET_ME_SP_PN ="get me shared preference preference name"
    const val GET_ME_SP ="get me shared preference"
    const val TOKEN_SP_PN= " token preference name"
    const val JWT_TOKEN_SP= " jwt token"
    const val REFRESH_TOKEN_SP= "refresh token"
      // aws
//    val baseUrl = "http://43.204.217.202:3200"
    // render
    val baseUrl = "https://career-connect-drxp.onrender.com"


    var client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
    fun checkForInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }
    fun parseErrorMessage(errorBody: String?): String? {
        errorBody?.let {
            try {
                val jsonObject = Gson().fromJson(it, JsonObject::class.java)
                return jsonObject?.get("message")?.asString
            } catch (e: Exception) {
                Log.e("rk", "Error parsing error message: ${e.message}")
            }
        }
        return null
    }
}