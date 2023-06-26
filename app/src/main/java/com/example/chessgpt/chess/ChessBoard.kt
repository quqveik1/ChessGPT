package com.example.chessgpt.chess

import android.graphics.Point
import androidx.core.graphics.plus
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
        // Черные фигуры
        board[0][0] = ChessPiece(ChessPieceType.ROOK, false)
        board[1][0] = ChessPiece(ChessPieceType.KNIGHT, false)
        board[2][0] = ChessPiece(ChessPieceType.BISHOP, false)
        board[3][0] = ChessPiece(ChessPieceType.QUEEN, false)
        board[4][0] = ChessPiece(ChessPieceType.KING, false)
        board[5][0] = ChessPiece(ChessPieceType.BISHOP, false)
        board[6][0] = ChessPiece(ChessPieceType.KNIGHT, false)
        board[7][0] = ChessPiece(ChessPieceType.ROOK, false)
        for (i in 0..7) {
            board[i][1] = ChessPiece(ChessPieceType.PAWN, false)
        }

// Белые фигуры
        board[0][7] = ChessPiece(ChessPieceType.ROOK, true)
        board[1][7] = ChessPiece(ChessPieceType.KNIGHT, true)
        board[2][7] = ChessPiece(ChessPieceType.BISHOP, true)
        board[3][7] = ChessPiece(ChessPieceType.QUEEN, true)
        board[4][7] = ChessPiece(ChessPieceType.KING, true)
        board[5][7] = ChessPiece(ChessPieceType.BISHOP, true)
        board[6][7] = ChessPiece(ChessPieceType.KNIGHT, true)
        board[7][7] = ChessPiece(ChessPieceType.ROOK, true)
        for (i in 0..7) {
            board[i][6] = ChessPiece(ChessPieceType.PAWN, true)
        }

// Пустые поля
        for (i in 0..7) {
            for (j in 2..5) {
                board[i][j] = ChessPiece(ChessPieceType.EMPTY, true)
            }
        }

    }

    fun getPossibleMoves(currentPosition: Point, isPawnMoveBottomToTop: Boolean = true): ArrayList<Point> {

        val isCurrentWhite = board[currentPosition.x][currentPosition.y]!!.isWhite
        val currCell = board[currentPosition.x][currentPosition.y]

        when (currCell!!.type) {
            ChessPieceType.PAWN -> {
                var pawnDelta: Int = -1;
                if(!isPawnMoveBottomToTop) pawnDelta *= 1;

                val moves = ArrayList<Point>()
                val currCheckPoint = Point(currentPosition.x, currentPosition.y + pawnDelta)

                for(i in 0 until 2)
                {
                    val res = checkPos(currCheckPoint, currCell)
                    if(res == PositionCheckRes.Yes || res == PositionCheckRes.Last)
                    {
                        moves.add(Point(currCheckPoint))
                    }
                    if(res == PositionCheckRes.No || res == PositionCheckRes.Last)
                    {
                        break
                    }
                }

                return moves

            }
            ChessPieceType.KNIGHT -> {

                val possibleDeltaMoves = arrayListOf(
                    Point(2, 1), Point(1, 2),
                    Point(2, -1), Point(1, -2),
                    Point(-2, 1), Point(-1, 2),
                    Point(-2, -1), Point(-1, -2)
                )

                val possibleFinalMoves = ArrayList<Point>(possibleDeltaMoves)

                for(i in 0 until possibleDeltaMoves.size)
                {
                    possibleFinalMoves[i] += currentPosition
                }

                validateArr(possibleFinalMoves, currCell)

                return possibleFinalMoves
            }
            ChessPieceType.BISHOP ->
            {
                val moves = ArrayList<Point>()

                addBishopMoves(moves, currentPosition, currCell)

                return moves
            }
            ChessPieceType.ROOK -> {
                // Логика для ходов ладьи
                // Возвращаем массив с возможными ходами

                val moves = ArrayList<Point>()

                addRookMoves(moves, currentPosition, currCell)

                return moves
            }
            ChessPieceType.QUEEN -> {
                val moves = ArrayList<Point>()

                addRookMoves(moves, currentPosition, currCell)
                addBishopMoves(moves, currentPosition, currCell)

                return moves
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

        return ArrayList()
    }

    private fun addRookMoves(moves: ArrayList<Point>, currentPosition: Point, currCell: ChessPiece)
    {
        val delta = Point(1, 0)
        val newPos = Point(currentPosition)

        doCheckStraightMovement(newPos, currCell, moves, delta)

        delta.x= -1;
        doCheckStraightMovement(newPos, currCell, moves, delta)

        delta.x= 0;
        delta.y= 1;
        doCheckStraightMovement(newPos, currCell, moves, delta)

        delta.y= -1;
        doCheckStraightMovement(newPos, currCell, moves, delta)
    }

    private fun addBishopMoves(moves: ArrayList<Point>, currentPosition: Point, currCell: ChessPiece)
    {
        var delta = Point(1, 1)
        var newPos = Point(currentPosition)

        doCheckStraightMovement(newPos, currCell, moves, delta)

        delta.y = -1
        doCheckStraightMovement(newPos, currCell, moves, delta)

        delta.x = -1
        doCheckStraightMovement(newPos, currCell, moves, delta)

        delta.x = 1
        delta.y = 1
        doCheckStraightMovement(newPos, currCell, moves, delta)
    }

    private fun doCheckStraightMovement(newPos: Point, currCell: ChessPiece, moves: ArrayList<Point>, delta: Point)
    {
        while (true)
        {
            newPos.x = newPos.x + delta.x;
            newPos.y = newPos.y + delta.y;
            val res = checkPos(newPos, currCell)

            if(res == PositionCheckRes.Yes || res == PositionCheckRes.Last)
            {
                moves.add(Point(newPos))
            }
            if(res == PositionCheckRes.No || res == PositionCheckRes.Last)
            {
                break
            }
        }
    }


    enum class PositionCheckRes
    {
        Yes,
        No,
        Last
    }

    private fun checkPos(pos: Point, currCell: ChessPiece) : PositionCheckRes
    {
        if(chessView.isValidPoint(pos))
        {
            if(board[pos.x][pos.y]!!.type == ChessPieceType.EMPTY)
            {
                return PositionCheckRes.Yes
            }

            if(board[pos.x][pos.y]!!.isWhite == currCell.isWhite)
            {
                return PositionCheckRes.No
            }
            else
            {
                return PositionCheckRes.Last
            }
        }
        return PositionCheckRes.No
    }

    private fun validateArr(arr: ArrayList<Point>, currCell: ChessPiece)
    {
        val iterator = arr.iterator()
        while (iterator.hasNext()) {
            val el = iterator.next()

            val res = checkPos(el, currCell)

            if(res == PositionCheckRes.No)
            {
                iterator.remove()
            }
        }
    }
}