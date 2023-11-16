package com.kurlic.chessgpt.gpt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.kurlic.chessgpt.R
import com.kurlic.chessgpt.chess.ChessBoard
import com.kurlic.chessgpt.chess.ChessView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GPTFragment : Fragment() {
    lateinit var chessView: ChessView
    lateinit var gameNameTextView: TextView

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val openAIChatAPI = retrofit.create(GPTApi::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView: View = inflater.inflate(R.layout.game_fragment, container, false)

        chessView = rootView.findViewById(R.id.chessView)
        chessView.loadBoardFromJson(null)

        return rootView
    }

    fun doAiMove() {
        val jsonBoard: String = chessView.saveBoardToJson()

        val messages = listOf(
            Message(
                role = "system",
                content = "Вы являетесь мощным шахматным AI. Пользователь предоставит вам текущее состояние шахматной доски в формате JSON, который включает массив доски 8x8, булевую переменную, указывающую, что активный игрок белый, и булевую переменную, указывающую, что нижняя сторона белая. Массив доски содержит объекты типа ChessPiece, каждый из которых представляет шахматную фигуру и ее цвет. Объекты ChessPiece типа EMPTY представляют пустые клетки на доске. Ваша задача - определить лучший следующий ход для активного игрока. Пожалуйста, верните свой ход в виде объекта JSON. Объект должен включать 'sx' и 'sy' (в диапазоне от 0 до 7), указывающие начальное положение фигуры, и 'dx' и 'dy' (также в диапазоне от 0 до 7), указывающие конечное положение после хода. Включите в этот JSON поле 'figure', которое указывает тип перемещаемой фигуры. Этот ход должен быть реальным и действительным. Помните, только конь может 'прыгать' через другие фигуры. Ваш ход не должен ставить вашего короля под угрозу шаха или матa."
            ),
            Message("user", jsonBoard))

        val requestBody = ChatRequest(messages)
        val call = openAIChatAPI.chatWithGPT3(requestBody)
        call.enqueue(object : Callback<ChatResponse> {
            override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                if (response.isSuccessful) {
                    val chatResponse = response.body()

                    val gptMoveStr: String = chatResponse!!.choices[0].message.content

                    val gson = Gson()
                    val gptMove = gson.fromJson(gptMoveStr, GPTMove::class.java)

                    Log.i("GPT", chatResponse.toString())

                    val isMoved = chessView.moveIfCan(gptMove)

                    if (!isMoved) doAiMove()

                } else {
                    Log.e("GPT", response.errorBody()!!.string())
                }
            }

            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                Log.e("GPT ERROR", t.toString())
            }
        })
    }
}