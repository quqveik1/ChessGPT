package com.kurlic.chessgpt.ai

import android.graphics.Point
import com.kurlic.chessgpt.chess.ChessBoard
import com.kurlic.chessgpt.chess.ChessPieceType
import kotlin.math.max
import kotlin.math.min

class ChessAI(var chessBoard: ChessBoard) {

    fun findBestMove(retStartPos: Point, retFinishPos: Point)
    {
        //minimax(4, true, retStartPos, retFinishPos)
        findBestMove(chessBoard.isActiveSideWhite, retStartPos, retFinishPos)
    }

    private fun findBestMove(isWhite: Boolean, retStartPos: Point, retFinishPos: Point) {
        var bestMoveScore: Int = -1
        val tmpStartPos = Point()
        var tmpFinishPos = Point(-1, -1)

        retFinishPos.x = -1
        retFinishPos.y = -1

        val bestMoves = mutableListOf<Pair<Point, Point>>()

        for(x in 0 until chessBoard.chessSize.width) {
            for(y in 0 until chessBoard.chessSize.height) {
                tmpStartPos.x = x
                tmpStartPos.y = y
                tmpFinishPos = Point(-1, -1)

                val points = checkCellMove(tmpStartPos,  tmpFinishPos,  isWhite)

                if(bestMoveScore < points)
                {
                    bestMoves.clear()
                    bestMoves.add(Pair(Point(tmpStartPos.x, tmpStartPos.y), Point(tmpFinishPos.x, tmpFinishPos.y)))
                    bestMoveScore = points
                }
                else if (bestMoveScore == points) {
                    bestMoves.add(Pair(Point(tmpStartPos.x, tmpStartPos.y), Point(tmpFinishPos.x, tmpFinishPos.y)))
                }
            }
        }

        if (bestMoves.isNotEmpty()) {
            val chosenMove = bestMoves.random()
            retStartPos.x = chosenMove.first.x
            retStartPos.y = chosenMove.first.y
            retFinishPos.x = chosenMove.second.x
            retFinishPos.y = chosenMove.second.y
        }
    }


    private fun checkCellMove(position: Point, retFinishPos: Point, isWhite: Boolean) : Int
    {
        if(!chessBoard.getCell(position).isSameSide(isWhite))
        {
            return -1;
        }

        val pMoves = chessBoard.getPossibleMoves(position)
        var bestMoveScore: Int = -1

        for(move in pMoves)
        {
            if(chessBoard.getCell(move).type.value > bestMoveScore)
            {
                bestMoveScore = (chessBoard.getCell(move).type.value);
                retFinishPos.x = move.x
                retFinishPos.y = move.y
            }
        }

        return bestMoveScore
    }

    private fun minimax(depth: Int, isMaximizingPlayer: Boolean, startRetPos: Point, finishRetPos: Point): Int {
        if (depth == 0) {
            return evaluateBoard()
        }

        var bestMoveScore = if (isMaximizingPlayer) Int.MIN_VALUE else Int.MAX_VALUE

        for (x in 0 until chessBoard.chessSize.width) {
            for (y in 0 until chessBoard.chessSize.height) {
                val startPos = Point(x, y)
                val possibleMoves = chessBoard.getPossibleMoves(startPos)

                for (move in possibleMoves) {
                    // Make the move on the board
                    val capturedPiece = chessBoard.getCell(move)
                    chessBoard.move(startPos, move, true)

                    // Call minimax recursively
                    val currentScore = minimax(depth - 1, !isMaximizingPlayer, startPos, move)

                    // Undo the move
                    chessBoard.move(move, startPos, true)
                    chessBoard.getCell(move).type = capturedPiece.type

                    // Update the best score and alpha/beta values
                    if (isMaximizingPlayer) {
                        if(currentScore > bestMoveScore)
                        {
                            bestMoveScore = currentScore
                            startRetPos.x = startPos.x
                            startRetPos.y = startPos.y
                            finishRetPos.x = move.x
                            finishRetPos.y = move.y
                        }
                    } else {
                        if(currentScore < bestMoveScore)
                        {
                            bestMoveScore = currentScore
                            startRetPos.x = startPos.x
                            startRetPos.y = startPos.y
                            finishRetPos.x = move.x
                            finishRetPos.y = move.y
                        }
                    }
                }
            }
        }

        return bestMoveScore
    }

    private fun evaluateBoard(): Int {
        var score = 0

        for (x in 0 until chessBoard.chessSize.width) {
            for (y in 0 until chessBoard.chessSize.height) {
                val piece = chessBoard.getCell(Point(x, y))
                if (piece.type != ChessPieceType.EMPTY) {

                    // Subtract the value if the piece is of the opposite color
                    score += if (piece.isWhite == chessBoard.isActiveSideWhite) piece.type.value else -piece.type.value
                }
            }
        }

        return score
    }


}