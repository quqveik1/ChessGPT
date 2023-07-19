package com.kurlic.chessgpt.online

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FindGameAPI {

    @GET("/find/game/new")
    fun findGameID(@Query("onlineID") onlineId: Long): Call<Long>

    @GET("/find/game/delete")
    fun deleteFromOnline(@Query("onlineID") onlineId: Long): Call<Boolean>
}