package com.depvt.live.football.tv.network

import com.depvt.live.football.tv.BuildConfig
import com.depvt.live.football.tv.utils.objects.Constants.baseIp
import com.depvt.live.football.tv.utils.objects.Constants.baseUrlDemo
import com.depvt.live.football.tv.utils.objects.Constants.cementMainType
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitController {

    ////Gson converter....
    private val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private val client: OkHttpClient by lazy {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }


        OkHttpClient.Builder()
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .connectTimeout(90, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            //.addInterceptor(RetrofitInterceptor("packageName"))
//                .authenticator(RetrofitAuthenticator())
            .build()
    }

    private val retrofitController_Channel: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(cementMainType)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }


    private val retrofitController_Ip: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(baseIp)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    private val retrofitController_Demo: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrlDemo)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    val apiServiceChannel: ApiInterface by lazy {
        retrofitController_Channel
            .build()
            .create(ApiInterface::class.java)
    }

    val apiServiceDemo: ApiInterface by lazy {
        retrofitController_Demo
            .build()
            .create(ApiInterface::class.java)
    }

    val apiServiceIP: ApiInterface by lazy {
        retrofitController_Ip
            .build()
            .create(ApiInterface::class.java)
    }

}