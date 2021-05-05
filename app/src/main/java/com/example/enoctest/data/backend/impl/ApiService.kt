package com.example.enoctest.data.backend.impl

import com.example.enoctest.common.AppConstants.Companion.API_KEY
import com.example.enoctest.data.backend.dto.LoginResponse
import com.example.enoctest.data.backend.dto.Upload
import com.example.enoctest.data.backend.dto.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    /*
        LOGIN
     */
    @FormUrlEncoded
    @POST("login.json")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginResponse>


    /*
        GET USER
     */
    @GET("user.json")
    suspend fun getUser(
        @Query("userId") userId: String,
        @Header("Authorization") token: String
    ): Response<User>


    /*
        UPLOAD IMAGE
     */
    @FormUrlEncoded
    @POST("upload.json")
    suspend fun upload(
        @Field("userId") userId: String,
        @Field("image") image: String,
        @Header("Authorization") token: String
    ): Response<Upload>
}