package com.kurlic.chessgpt.game

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.kurlic.chessgpt.MainActivity
import com.kurlic.chessgpt.R
import com.kurlic.chessgpt.chess.ChessBoard
import com.kurlic.chessgpt.chess.ChessMoveListener
import com.kurlic.chessgpt.chess.ChessView
import com.kurlic.chessgpt.localgames.LocalGameDao
import com.kurlic.chessgpt.localgames.LocalGameDataBase

abstract class GameFragment:Fragment()
{
    companion object
    {
        const val ID_KEY = "id"
    }

    var gameId: Int = -1;

    lateinit var localGameDao: LocalGameDao

    override fun onAttach(context: Context)
    {
        super.onAttach(context)
        if(context is MainActivity)
        {
            localGameDao = LocalGameDataBase.getDatabase(context).localGameDao()
        }
    }

    lateinit var chessView: ChessView
    lateinit var gameNameTextView: TextView
    lateinit var activeMoveSideTextView: TextView

    private val whiteMove: String by lazy {
        requireContext().getString(R.string.white_move)
    }
    private val blackMove: String  by lazy {
        requireContext().getString(R.string.black_move)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        val rootView: View = inflater.inflate(R.layout.game_fragment, container, false)

        gameNameTextView = rootView.findViewById(R.id.gameName)
        chessView = rootView.findViewById(R.id.chessView)
        activeMoveSideTextView = rootView.findViewById(R.id.activeMoveSide)

        chessView.moveListener = getChessMoveListener()

        onCreate()

        loadBoard(savedInstanceState)

        return rootView
    }

    open inner class GameMoveListener : ChessMoveListener
    {
        override fun onMoveMade(chessBoard: ChessBoard) {
            setActiveColor(chessBoard)
            saveBoardBetweenMoves()

        }

        override fun onArrangementMade(chessBoard: ChessBoard)
        {
            setActiveColor(chessBoard)
        }

        override fun onGameEnded(isWinSideWhite: Boolean) {

            val winner = if (isWinSideWhite) "White" else "Black"

            AlertDialog.Builder(requireContext()).apply {
                setTitle("Game Ended")
                setMessage("$winner side won! Congratulations!")
                setPositiveButton("OK") { _, _ ->
                    parentFragmentManager.popBackStack()
                }.setOnCancelListener{parentFragmentManager.popBackStack()}
            }.create().show()
        }
    }


    open fun onCreate() {}
    abstract fun loadBoard(savedInstanceState: Bundle?)
    open fun getChessMoveListener() : GameMoveListener?{return GameMoveListener()}
    open fun saveBoardBetweenMoves() {}
    abstract fun saveBoardOnDestroyView(outState: Bundle)

    fun setActiveColor(chessBoard: ChessBoard)
    {
        if(chessBoard.isActiveSideWhite)
        {
            activeMoveSideTextView.text = whiteMove
        }
        else
        {
            activeMoveSideTextView.text = blackMove
        }
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)

        saveBoardOnDestroyView(outState)
    }
}