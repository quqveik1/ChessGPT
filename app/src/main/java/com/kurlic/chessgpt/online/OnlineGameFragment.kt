package com.kurlic.chessgpt.online

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kurlic.chessgpt.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnlineGameFragment : Fragment() {

    var onlineID: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.findgame_fragment, container, false)

        connectToGetId()

        return rootView
    }

    fun connectToGetId()
    {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.10.102:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(PlayerAPI::class.java)

        val androidId = Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)

        val call = api.getPlayerID(androidId)
        call.enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (response.isSuccessful) {
                    onlineID = response.body()
                    Toast.makeText(context, onlineID.toString(), Toast.LENGTH_SHORT).show()
                } else {
                    // Здесь вы можете обработать ошибку ответа HTTP
                    Log.e("context", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
                // Здесь вы можете обработать ошибку сети или другую ошибку во время выполнения
                Toast.makeText(context, "Failure: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                Log.e("context", "Error: ${t.localizedMessage}")
            }
        })
    }


    private fun handleMessage(message: String) {
        // TODO: Handle the received message
        Log.d("CHESSSERVER", "Received message: $message")
        // Дополнительная обработка полученного сообщения
    }

}
