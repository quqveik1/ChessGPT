package com.example.chessgpt.chess

import android.graphics.Point

data class ChessPiece(
    var type: ChessPieceType,
    var isWhite: Boolean
)
{
    public fun isSameSide(chessPiece: ChessPiece) : Boolean
    {
        return  isWhite == chessPiece.isWhite
    }
    public fun isEnemy(chessPiece: ChessPiece) : Boolean
    {
        return  isWhite != chessPiece.isWhite
    }

}
