package com.example.chessgpt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chessgpt.chess.ChessView

class MainActivity : AppCompatActivity() {

    lateinit var chessView: ChessView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chessView = findViewById(R.id.chessView)
    }

    override fun onStop() {
        super.onStop()
        chessView.saveBoard()
    }
}