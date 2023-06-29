package com.kurlic.chessgpt.chess

enum class ChessPieceType(val symbol: String) {
    PAWN("P"),
    KNIGHT("N"),
    BISHOP("B"),
    ROOK("R"),
    QUEEN("Q"),
    KING("K"),
    EMPTY(" ");
}