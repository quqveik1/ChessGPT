package com.kurlic.chessgpt.chess

interface ChessMoveListener
{
    fun onMoveMade(chessBoard: ChessBoard)

    fun onArrangementMade(chessBoard: ChessBoard)

    fun onGameEnded(isWinSideWhite: Boolean)
}