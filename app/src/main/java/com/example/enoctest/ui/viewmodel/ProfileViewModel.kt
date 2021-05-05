package com.example.enoctest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.enoctest.common.Resource
import com.example.enoctest.data.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {

    lateinit var token: String
    lateinit var userId: String

    fun getUser() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = profileRepository.getUser(userId, token)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun uploadImage(base64Image: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = profileRepository.uploadImage(base64Image, userId, token)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}