package com.kurlic.chessgpt.ai

import android.graphics.Point
import android.os.Bundle
import com.kurlic.chessgpt.R
import com.kurlic.chessgpt.chess.ChessBoard
import com.kurlic.chessgpt.game.GameFragment

class AIFragment : GameFragment() {
    lateinit var chessAI: ChessAI

    val boardKey = "board"

    override fun onCreate() {
        super.onCreate()

        gameNameTextView.setText(R.string.game_vs_ai)
    }

    inner class AIMoveListener : GameMoveListener() {
        override fun onMoveMade(chessBoard: ChessBoard) {
            super.onMoveMade(chessBoard)
            if (chessBoard.isActiveSideWhite != chessBoard.isBottomSideWhite) {
                doAiMove()
            }
        }

        override fun onArrangementMade(chessBoard: ChessBoard) {
            super.onArrangementMade(chessBoard)

            if (chessBoard.isActiveSideWhite != chessBoard.isBottomSideWhite) {
                doAiMove()
            }
        }
    }

    override fun getChessMoveListener(): GameMoveListener? {
        return AIMoveListener()
    }

    override fun loadBoard(savedInstanceState: Bundle?) {
        val json = savedInstanceState?.getString(boardKey)
        chessAI = ChessAI(chessView.chessBoard)

        if (isBottomSideWhite != null) {
            chessView.loadBoardFromJson(json, isBottomSideWhite!!)
        } else {
            chessView.loadBoardFromJson(json)
        }
    }

    override fun saveBoardOnDestroyView(outState: Bundle) {
        val json = chessView.saveBoardToJson()

        outState.putString(boardKey, json)
    }

    private fun doAiMove() {
        val start = Point()
        val finish = Point(-1, -1)

        chessAI.findBestMove(start, finish)

        if (chessView.chessBoard.isValidPoint(finish)) {
            chessView.doMove(start, finish)
        }
    }
}