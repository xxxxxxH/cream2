package com.sherloki.devkit

import com.sherloki.devkit.entity.ResultBean
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface DevkitService {

    @POST("config")
    suspend fun getConfig(): ResponseBody?

    @POST
    suspend fun uploadFbData(
        @Url url: String,
        @Body body: Map<String, String>,
    ): ResultBean?
}