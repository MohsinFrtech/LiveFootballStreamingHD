package com.depvt.live.football.tv.network

import com.depvt.live.football.tv.models.DataStone
import com.depvt.live.football.tv.models.DataStone2
import com.depvt.live.football.tv.utils.objects.Constants.baseIp
import com.depvt.live.football.tv.utils.objects.Constants.channelApi
import com.depvt.live.football.tv.utils.objects.Constants.userApi
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @GET(baseIp)
    fun getIPAsync(): Call<String?>?


    @POST(userApi)
    @Headers("Content-Type: application/json")
    fun getDemoDataAsync(
        @Body body: RequestBody
    ): Call<DataStone2?>

    @POST(channelApi)
    @Headers("Content-Type: application/json")
    fun getChannelsAsync(
        @Body body: RequestBody
    ): Call<DataStone>


}