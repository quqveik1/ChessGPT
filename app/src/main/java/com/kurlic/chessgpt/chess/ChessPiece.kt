package com.kurlic.chessgpt.chess

import android.graphics.Point

data class ChessPiece(
    var type: ChessPieceType,
    var isWhite: Boolean,
    var hasMoved: Boolean = false
)
{
    fun getCopyForMove() : ChessPiece
    {
        hasMoved = true

        return this.copy()
    }

    fun isSameSide(chessPiece: ChessPiece) : Boolean
    {
        return  isWhite == chessPiece.isWhite
    }

    fun isSameSide(isWhite: Boolean) : Boolean
    {
        return  this.isWhite == isWhite
    }

    fun isEnemy(chessPiece: ChessPiece) : Boolean
    {
        return  isWhite != chessPiece.isWhite
    }

}
