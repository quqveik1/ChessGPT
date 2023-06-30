package com.kurlic.chessgpt.gpt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kurlic.chessgpt.R
import com.kurlic.chessgpt.chess.ChessView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GPTFragment : Fragment()
{
    lateinit var chessView: ChessView
    lateinit var gameNameTextView: TextView

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val openAIChatAPI = retrofit.create(GPTApi::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val rootView: View = inflater.inflate(R.layout.game_fragment, container, false)

        chessView = rootView.findViewById(R.id.chessView)
        chessView.loadBoardFromJson(null)

        val chatGptMove: Button = rootView.findViewById(R.id.gptMove)

        chatGptMove.setOnClickListener {
            val messages = listOf( Message("system", "You are a good chess player. User will send "),
                Message("user", "Hello! Translate Hello from Russian"))

            val requestBody = ChatRequest(messages)
            val call = openAIChatAPI.chatWithGPT3(requestBody)
            call.enqueue(object : Callback<ChatResponse>
            {
                override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                    if (response.isSuccessful)
                    {
                        val chatResponse = response.body()

                        Log.i("GPT", "GOOD CALLBACK")
                    }
                    else
                    {
                        Log.e("GPT", response.errorBody()!!.string())
                    }
                }

                override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                    Log.e("GPT ERROR", t.toString())
                }
            })
        }

        return rootView
    }
}