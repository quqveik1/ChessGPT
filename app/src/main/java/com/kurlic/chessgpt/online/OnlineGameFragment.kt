package com.kurlic.chessgpt.online

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.kurlic.chessgpt.R
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.LifecycleRegistry
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.timer

class OnlineGameFragment : Fragment() {

    var onlineID: Long? = null
    lateinit var retryButton: Button
    lateinit var progressBar: ProgressBar
    lateinit var searchData: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.findgame_fragment, container, false)

        retryButton = rootView.findViewById(R.id.retryButton)
        progressBar = rootView.findViewById(R.id.progressBar)
        searchData = rootView.findViewById(R.id.searchData)

        retryButton.setOnClickListener{
            connectToGetId()
        }

        connectToGetId()

        return rootView
    }

    val ServerIP = "192.168.1.119:8080"

    @SuppressLint("HardwareIds")
    fun connectToGetId()
    {
        onSearchStartInfo()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://$ServerIP/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(PlayerAPI::class.java)

        val androidId = Settings.Secure.getString(context?.contentResolver, Settings.Secure.ANDROID_ID)

        val call = api.getPlayerID(androidId)
        call.enqueue(object : Callback<Long>
        {
            override fun onResponse(call: Call<Long>, response: Response<Long>)
            {
                if (!isAdded) return
                if (response.isSuccessful)
                {
                    onlineID = response.body()
                    Toast.makeText(context, onlineID.toString(), Toast.LENGTH_SHORT).show()
                    onWellServerResponse()
                }
                else
                {
                    Log.e("context", "Error: ${response.code()}")
                    serverResponseErrorInfo()
                }
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {
                if (!isAdded) return
                Toast.makeText(context, "Failure: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                Log.e("context", "Error: ${t.localizedMessage}")
                serverResponseErrorInfo()
            }
        })
    }

    private fun onSearchStartInfo()
    {
        progressBar.visibility = View.VISIBLE
        retryButton.visibility = View.GONE

        searchData.text = "Подключение к серверу"
        searchData.setTextColor(ContextCompat.getColor(requireContext(), R.color.textColor))

    }

    private fun onWellServerResponse()
    {
        progressBar.visibility = View.VISIBLE
        retryButton.visibility = View.GONE

        searchData.text = "Поиск игры"
        searchData.setTextColor(ContextCompat.getColor(requireContext(), R.color.textColor))

    }

    private fun findGame()
    {

    }

    private fun serverResponseErrorInfo()
    {
        progressBar.visibility = View.GONE
        retryButton.visibility = View.VISIBLE

        searchData.text = "Ошибка подключения к серверу"
        searchData.setTextColor(ContextCompat.getColor(requireContext(), R.color.errorColor))
    }


    private fun handleMessage(message: String) {
        // TODO: Handle the received message
        Log.d("CHESSSERVER", "Received message: $message")
        // Дополнительная обработка полученного сообщения
    }

}
