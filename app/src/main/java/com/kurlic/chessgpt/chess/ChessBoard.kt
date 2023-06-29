package com.kurlic.chessgpt.chess

import android.graphics.Point
import android.util.Size
import androidx.core.graphics.minus
import androidx.core.graphics.plus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

class ChessBoard : Serializable {

    public val chessSize: SizeSeriazable = SizeSeriazable(8, 8)
    public var isActiveSideWhite: Boolean = true;
    public var isBottomSideWhite: Boolean = true;

    var board: Array<Array<ChessPiece>> = Array(8) {
        Array(8) { ChessPiece(ChessPieceType.EMPTY, true) }
    }

    public fun initBoard(isBottomSideWhite: Boolean = true)
    {
        this.isBottomSideWhite = isBottomSideWhite
        defaultInit()
    }

    public fun toJson() : String
    {
        val gson = Gson()
        val chessBoardJson = gson.toJson(this)

        return chessBoardJson
    }
    private fun defaultInit()
    {

        board[0][0] = ChessPiece(ChessPieceType.ROOK, !isBottomSideWhite)
        board[1][0] = ChessPiece(ChessPieceType.KNIGHT, !isBottomSideWhite)
        board[2][0] = ChessPiece(ChessPieceType.BISHOP, !isBottomSideWhite)
        board[3][0] = ChessPiece(ChessPieceType.QUEEN, !isBottomSideWhite)
        board[4][0] = ChessPiece(ChessPieceType.KING, !isBottomSideWhite)
        board[5][0] = ChessPiece(ChessPieceType.BISHOP, !isBottomSideWhite)
        board[6][0] = ChessPiece(ChessPieceType.KNIGHT, !isBottomSideWhite)
        board[7][0] = ChessPiece(ChessPieceType.ROOK, !isBottomSideWhite)
        for (i in 0 until chessSize.width)
        {
            board[i][1] = ChessPiece(ChessPieceType.PAWN, !isBottomSideWhite)
        }

        board[0][7] = ChessPiece(ChessPieceType.ROOK, isBottomSideWhite)
        board[1][7] = ChessPiece(ChessPieceType.KNIGHT, isBottomSideWhite)
        board[2][7] = ChessPiece(ChessPieceType.BISHOP, isBottomSideWhite)
        board[3][7] = ChessPiece(ChessPieceType.QUEEN, isBottomSideWhite)
        board[4][7] = ChessPiece(ChessPieceType.KING, isBottomSideWhite)
        board[5][7] = ChessPiece(ChessPieceType.BISHOP, isBottomSideWhite)
        board[6][7] = ChessPiece(ChessPieceType.KNIGHT, isBottomSideWhite)
        board[7][7] = ChessPiece(ChessPieceType.ROOK, isBottomSideWhite)
        for (i in 0 until chessSize.width)
        {
            board[i][chessSize.height - 2] = ChessPiece(ChessPieceType.PAWN, isBottomSideWhite)
        }

        for (i in 0 until chessSize.width)
        {
            for (j in 2 until chessSize.height - 2)
            {
                board[i][j] = ChessPiece(ChessPieceType.EMPTY, true)
            }
        }

    }

    fun getPossibleMoves(currentPosition: Point): ArrayList<Point>
    {
        val currCell = board[currentPosition.x][currentPosition.y]
        if(currCell.isWhite != isActiveSideWhite) return ArrayList();

        when (currCell.type) {
            ChessPieceType.PAWN -> {
                var pawnDelta: Int = getPawnDir()

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
                    currCheckPoint.y += pawnDelta
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
                val moves = ArrayList<Point>()

                var newPos = Point(currentPosition)
                newPos.x -= 2
                newPos.y -= 1

                for(i in 0 until 3)
                {
                    newPos.x += 1;
                    if(checkPos(newPos, currCell) != PositionCheckRes.No)
                    {
                        moves.add(Point(newPos))
                    }
                }

                for(i in 1 until 3)
                {
                    newPos.y += 1;
                    if(checkPos(newPos, currCell) != PositionCheckRes.No)
                    {
                        moves.add(Point(newPos))
                    }
                }

                for(i in 1 until 3)
                {
                    newPos.x -= 1;
                    if(checkPos(newPos, currCell) != PositionCheckRes.No)
                    {
                        moves.add(Point(newPos))
                    }
                }

                for(i in 1 until 2)
                {
                    newPos.y -= 1;
                    if(checkPos(newPos, currCell) != PositionCheckRes.No)
                    {
                        moves.add(Point(newPos))
                    }
                }

                return moves
            }
            ChessPieceType.EMPTY -> {
                // Возвращаем пустой массив для пустой клетки
                return ArrayList()
            }
        }
    }

    private fun getPawnDir() : Int
    {
        if(isBottomSideWhite)
        {
            if(isActiveSideWhite)
            {
                return -1;
            }
            else
            {
                return 1;
            }
        }
        else
        {
            if(isActiveSideWhite)
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }
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

        delta.x = -1
        delta.y = 1
        doCheckStraightMovement(newPos, currCell, moves, delta)
    }

    private fun doCheckStraightMovement(pos: Point, currCell: ChessPiece, moves: ArrayList<Point>, delta: Point)
    {
        val newPos = Point(pos)
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
        if(isValidPoint(pos))
        {
            if(board[pos.x][pos.y].type == ChessPieceType.EMPTY)
            {
                return PositionCheckRes.Yes
            }

            if(board[pos.x][pos.y].isSameSide(currCell))
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

    public fun canMove(possibleMoves: ArrayList<Point>, newPos: Point, currPos: Point) : Boolean
    {
        if(board[currPos.x][currPos.y].isWhite == isActiveSideWhite)
        {
            val res = possibleMoves.contains(newPos)

            return res
        }

        return false
    }

    public fun move(lastPos: Point, newPos: Point)
    {
        //val areEnemies = board[lastPos.x][lastPos.y].isEnemy(board[newPos.x][newPos.y])

        board[newPos.x][newPos.y] = board[lastPos.x][lastPos.y].copy();
        board[lastPos.x][lastPos.y].type = ChessPieceType.EMPTY

        isActiveSideWhite = !isActiveSideWhite;
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

    fun isValidPoint(pos: Point) : Boolean
    {
        if(pos.x < 0) return false
        if(pos.y < 0) return false
        if(pos.x >= chessSize.width) return false
        if(pos.y >= chessSize.height) return false

        return true
    }
}