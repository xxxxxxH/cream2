package com.sherloki.devkit

import com.sherloki.devkit.entity.ResultBean
import com.sherloki.devkit.entity.StatusBean
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface DevkitService {

    @POST("config")
    suspend fun getConfig(): ResponseBody?

    @POST
    suspend fun uploadFbData(
        @Url url: String,
        @Body body: Map<String,String>,
    ): ResultBean?
}