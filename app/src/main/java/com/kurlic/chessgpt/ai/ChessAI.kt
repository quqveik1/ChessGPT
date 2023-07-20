package com.kurlic.chessgpt.ai

import android.graphics.Point
import com.kurlic.chessgpt.chess.ChessBoard

class ChessAI(var chessBoard: ChessBoard) {

    fun findBestMove(retStartPos: Point, retFinishPos: Point)
    {
        findBestMove(chessBoard.isActiveSideWhite, retStartPos, retFinishPos)
    }

    fun findBestMove(isWhite: Boolean, retStartPos: Point, retFinishPos: Point) {
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


}