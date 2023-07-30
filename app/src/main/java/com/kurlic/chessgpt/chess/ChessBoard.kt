package com.kurlic.chessgpt.chess

import android.graphics.Point
import android.util.Size
import androidx.core.graphics.minus
import androidx.core.graphics.plus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import kotlin.math.abs

class ChessBoard : Serializable {

    val chessSize: SizeSeriazable = SizeSeriazable(8, 8)
    var isActiveSideWhite: Boolean = true
    var isBottomSideWhite: Boolean = true

    @Transient
    var chessMoveListener: ChessMoveListener? = null

    var board: Array<Array<ChessPiece>> = Array(8) {
        Array(8) { ChessPiece(ChessPieceType.EMPTY, true) }
    }

    fun getCell(point: Point) : ChessPiece
    {
        return board[point.x][point.y]
    }

    fun initBoard(isBottomSideWhite: Boolean = true)
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
                val pawnDelta: Int = getPawnDir()

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

                val movePos = Point(currentPosition)
                movePos.y += pawnDelta
                movePos.x--

                val res1 = checkPos(movePos, currCell)
                if(res1 == PositionCheckRes.Last)
                {
                    moves.add(Point(movePos))
                }

                movePos.x+=2

                val res2 = checkPos(movePos, currCell)
                if(res2 == PositionCheckRes.Last)
                {
                    moves.add(Point(movePos))
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

                val newPos = Point(currentPosition)
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

                checkCastling(currentPosition, moves)

                return moves
            }
            ChessPieceType.EMPTY -> {
                // Возвращаем пустой массив для пустой клетки
                return ArrayList()
            }
        }
    }

    private fun getRelativeRight(currentPosition: Point) : Int
    {
        return if( !(isBottomSideWhite xor getCell(currentPosition).isWhite)) 1 else -1
    }

    private fun getRelativeStart(currentPosition: Point) : Int
    {
        return if (!(getCell(currentPosition).isWhite xor isBottomSideWhite)) 0 else chessSize.width - 1
    }
    private fun getRelativeFinish(currentPosition: Point) : Int
    {
        return if (!(getCell(currentPosition).isWhite xor  isBottomSideWhite))  chessSize.width - 1 else 0
    }
    private fun getRelativeBottom(currentPosition: Point) : Int
    {
        return if (!(getCell(currentPosition).isWhite xor isBottomSideWhite))  chessSize.height - 1 else 0
    }
    private fun getRelativeTop(currentPosition: Point) : Int
    {
        return if (!(getCell(currentPosition).isWhite xor isBottomSideWhite)) 0 else chessSize.height - 1
    }

    private fun checkCastling(currentPosition: Point, moves: ArrayList<Point>)
    {
        if(!getCell(currentPosition).hasMoved)
        {
            var checkablePos = Point(currentPosition.x, currentPosition.y)
            val rightDelta = getRelativeRight(currentPosition)

            var hasNeighbours = false
            for(x in 1 .. 2)
            {
                checkablePos.x += rightDelta
                if(getCell(checkablePos).type != ChessPieceType.EMPTY)
                {
                    hasNeighbours = true
                    break
                }
            }

            if(!hasNeighbours)
            {
                moves.add(Point(currentPosition.x + rightDelta * 2, checkablePos.y))
            }

            hasNeighbours = false
            checkablePos = Point(currentPosition.x, currentPosition.y)

            for(x in 1 .. 3)
            {
                checkablePos.x -= rightDelta
                if(getCell(checkablePos).type != ChessPieceType.EMPTY)
                {
                    hasNeighbours = true
                    break
                }
            }

            if(!hasNeighbours)
            {
                moves.add(Point(currentPosition.x - rightDelta * 2, checkablePos.y))
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
        val delta = Point(1, 1)
        val newPos = Point(currentPosition)

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

    fun canMove(possibleMoves: ArrayList<Point>, newPos: Point, currPos: Point) : Boolean
    {
        if(board[currPos.x][currPos.y].isWhite == isActiveSideWhite)
        {
            val res = possibleMoves.contains(newPos)

            return res
        }

        return false
    }

    fun move(lastPos: Point, newPos: Point, needToChangeActiveSide: Boolean = true)
    {
        val areEnemies = board[lastPos.x][lastPos.y].isEnemy(board[newPos.x][newPos.y])
        if(areEnemies && getCell(newPos).type == ChessPieceType.KING)
        {
            chessMoveListener?.onGameEnded(isActiveSideWhite)
        }

        if(board[lastPos.x][lastPos.y].type == ChessPieceType.KING)
        {
            val delta = newPos.x - lastPos.x
            if(abs(delta) > 1)
            {
                val rightDir = getRelativeRight(lastPos)

                val isRightRook = (delta / rightDir) >= 0

                val bottom = getRelativeBottom(lastPos)

                val rookStartPos = Point(if(isRightRook) getRelativeFinish(lastPos) else getRelativeStart(lastPos),
                    bottom)

                val rookFinishPos = Point(if(isRightRook) newPos.x - rightDir else newPos.x + rightDir,
                    bottom)

                move(rookStartPos, rookFinishPos, false)
            }
        }

        board[newPos.x][newPos.y] = board[lastPos.x][lastPos.y].getCopyForMove()
        board[lastPos.x][lastPos.y].type = ChessPieceType.EMPTY

        if(needToChangeActiveSide) isActiveSideWhite = !isActiveSideWhite;
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