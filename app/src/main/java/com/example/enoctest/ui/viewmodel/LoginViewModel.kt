package com.example.enoctest.ui.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.enoctest.common.Resource

import com.example.enoctest.data.repository.LoginRepository
import kotlinx.coroutines.Dispatchers

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    fun login(username: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = loginRepository.login(username, password)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun saveToken(context: Context, token: String, userId: String) {
        // save in shared preferences
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("EnocTest", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.putString("userId", userId)
        editor.apply()
        editor.commit()
    }

    fun checkToken(context: Context): Boolean {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("EnocTest", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "") ?: return false

        return token.isNotEmpty()
    }
}