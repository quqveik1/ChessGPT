package com.example.chessgpt.chess

import android.graphics.Point
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ChessBoard {

    private val chessView: ChessView;

    constructor(chessView: ChessView)
    {
        this.chessView = chessView;
    }

    var board: Array<Array<ChessPiece?>> = Array(8) { arrayOfNulls<ChessPiece>(8) }

    public fun initBoard(json: String?) {

        if(json == null)
        {
            defaultInit()
            return
        }

        val type = object : TypeToken<Array<Array<ChessPiece?>>>() {}.type
        val gson = Gson()
        board = gson.fromJson(json, type)
    }

    private fun defaultInit()
    {
        // Черные фигуры
        board[0][0] = ChessPiece(ChessPieceType.ROOK, false)
        board[0][1] = ChessPiece(ChessPieceType.KNIGHT, false)
        board[0][2] = ChessPiece(ChessPieceType.BISHOP, false)
        board[0][3] = ChessPiece(ChessPieceType.QUEEN, false)
        board[0][4] = ChessPiece(ChessPieceType.KING, false)
        board[0][5] = ChessPiece(ChessPieceType.BISHOP, false)
        board[0][6] = ChessPiece(ChessPieceType.KNIGHT, false)
        board[0][7] = ChessPiece(ChessPieceType.ROOK, false)
        for (i in 0..7) {
            board[1][i] = ChessPiece(ChessPieceType.PAWN, false)
        }

        // Белые фигуры
        board[7][0] = ChessPiece(ChessPieceType.ROOK, true)
        board[7][1] = ChessPiece(ChessPieceType.KNIGHT, true)
        board[7][2] = ChessPiece(ChessPieceType.BISHOP, true)
        board[7][3] = ChessPiece(ChessPieceType.QUEEN, true)
        board[7][4] = ChessPiece(ChessPieceType.KING, true)
        board[7][5] = ChessPiece(ChessPieceType.BISHOP, true)
        board[7][6] = ChessPiece(ChessPieceType.KNIGHT, true)
        board[7][7] = ChessPiece(ChessPieceType.ROOK, true)
        for (i in 0..7) {
            board[6][i] = ChessPiece(ChessPieceType.PAWN, true)
        }

        // Пустые поля
        for (i in 2..5) {
            for (j in 0..7) {
                board[i][j] = ChessPiece(ChessPieceType.EMPTY, true)
            }
        }

    }

    fun getPossibleMoves(currentPosition: Point, isPawnMoveBottomToTop: Boolean = true): ArrayList<Point> {
        // Логика для определения возможных ходов в зависимости от типа фигуры

        when (board[currentPosition.x][currentPosition.y]!!.type) {
            ChessPieceType.PAWN -> {
                var pawnDelta: Int = 1;
                if(!isPawnMoveBottomToTop) pawnDelta *= -1;

                var moves = arrayListOf(
                    Point(currentPosition.x, currentPosition.y + pawnDelta),
                    Point(currentPosition.x, currentPosition.y + pawnDelta * 2)
                )

                validateArr(moves)

                return moves

            }
            ChessPieceType.KNIGHT -> {

                val possibleDeltaMoves = arrayListOf(
                    Point(2, 1), Point(1, 2),
                    Point(2, -1), Point(1, -2),
                    Point(-2, 1), Point(-1, 2),
                    Point(-2, -1), Point(-1, -2)
                )

                val possibleFinalMoves = ArrayList<Point>()

                possibleFinalMoves.addAll(possibleDeltaMoves)
                possibleFinalMoves.add(currentPosition)

                validateArr(possibleFinalMoves)

                return possibleFinalMoves
            }
            ChessPieceType.BISHOP -> {
                // Логика для ходов слона
                // Возвращаем массив с возможными ходами
                // ...
            }
            ChessPieceType.ROOK -> {
                // Логика для ходов ладьи
                // Возвращаем массив с возможными ходами
                // ...
            }
            ChessPieceType.QUEEN -> {
                // Логика для ходов ферзя
                // Возвращаем массив с возможными ходами
                // ...
            }
            ChessPieceType.KING -> {
                // Логика для ходов короля
                // Возвращаем массив с возможными ходами
                // ...
            }
            ChessPieceType.EMPTY -> {
                // Возвращаем пустой массив для пустой клетки
                return ArrayList()
            }
        }
    }

    private fun validateArr(arr: ArrayList<Point>)
    {
        val iterator = arr.iterator()
        while (iterator.hasNext()) {
            val el = iterator.next()
            if (!chessView.isValidPoint(el)) {
                iterator.remove()
            }
        }
    }
}