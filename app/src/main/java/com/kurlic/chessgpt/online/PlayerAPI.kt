package com.kurlic.chessgpt.online

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query


interface PlayerAPI {
    @GET("/init/player")
    fun getPlayerID(@Query("androidID") androidID: String): Call<Long>
}