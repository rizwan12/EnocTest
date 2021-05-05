package com.example.enoctest.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.enoctest.data.backend.impl.ApiHelper
import com.example.enoctest.data.repository.LoginRepository
import com.example.enoctest.ui.viewmodel.LoginViewModel

class LoginViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(LoginRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}

