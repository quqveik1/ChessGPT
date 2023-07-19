package com.kurlic.chessgpt.online

import retrofit2.Retrofit

class OnlineGameData {

    companion object
    {
        var onlineID: Long? = null

        var onlineGameID: Long? = null
        var retrofit: Retrofit? = null
    }

}