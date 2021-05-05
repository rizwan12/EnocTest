package com.example.enoctest.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.enoctest.data.backend.impl.ApiHelper
import com.example.enoctest.data.repository.LoginRepository
import com.example.enoctest.data.repository.ProfileRepository
import com.example.enoctest.ui.viewmodel.LoginViewModel
import com.example.enoctest.ui.viewmodel.ProfileViewModel

class ProfileViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(ProfileRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}