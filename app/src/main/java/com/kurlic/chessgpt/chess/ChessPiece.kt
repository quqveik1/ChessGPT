package com.kurlic.chessgpt.chess

import android.graphics.Point

data class ChessPiece(
    var type: ChessPieceType,
    var isWhite: Boolean
)
{
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
