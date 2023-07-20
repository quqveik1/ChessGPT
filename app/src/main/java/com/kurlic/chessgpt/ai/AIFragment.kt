package com.kurlic.chessgpt.ai

import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.kurlic.chessgpt.GameFragment
import com.kurlic.chessgpt.R
import com.kurlic.chessgpt.chess.ChessBoard
import com.kurlic.chessgpt.chess.ChessView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AIFragment : GameFragment()
{
    lateinit var  chessAI: ChessAI

    val boardKey = "board"

    override fun onCreate()
    {
        super.onCreate()

    }

    override fun loadBoard(savedInstanceState: Bundle?)
    {
        val json = savedInstanceState?.getString(boardKey)

        chessView.loadBoardFromJson(json)

        chessAI = ChessAI(chessView.chessBoard)
    }

    override fun saveBoardOnDestroyView(outState: Bundle)
    {
        val json = chessView.saveBoardToJson()

        outState.putString(boardKey, json)
    }


    private fun doAiMove()
    {
        val start = Point()
        val finish = Point(-1, -1)

        chessAI.findBestMove(start, finish)

        if(chessView.chessBoard.isValidPoint(finish))
        {
            chessView.doMove(start, finish)
        }



    }
}