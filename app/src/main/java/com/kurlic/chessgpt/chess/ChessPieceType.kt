package com.kurlic.chessgpt.chess

enum class ChessPieceType(val symbol: String) {
    PAWN("pawn"),
    KNIGHT("knight"),
    BISHOP("bishop"),
    ROOK("rook"),
    QUEEN("queen"),
    KING("king"),
    EMPTY(" ");
}