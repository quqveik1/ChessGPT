package com.kurlic.chessgpt.chess

enum class ChessPieceType(val symbol: String, val value: Int) {
    PAWN("pawn", 1),
    KNIGHT("knight", 3),
    BISHOP("bishop", 3),
    ROOK("rook", 5),
    QUEEN("queen", 9),
    KING("king", 100),
    EMPTY(" ", 0);
}