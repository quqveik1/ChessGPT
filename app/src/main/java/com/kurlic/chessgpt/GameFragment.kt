package com.kurlic.chessgpt

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.kurlic.chessgpt.chess.ChessBoard
import com.kurlic.chessgpt.chess.ChessMoveListener
import com.kurlic.chessgpt.chess.ChessView
import com.kurlic.chessgpt.localgames.LocalGame
import com.kurlic.chessgpt.localgames.LocalGameDao
import com.kurlic.chessgpt.localgames.LocalGameDataBase
import kotlinx.coroutines.launch

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

        onCreate()

        loadBoard(savedInstanceState)

        chessView.moveListener = object : ChessMoveListener {
            override fun onMoveMade(chessBoard: ChessBoard) {
                setActiveColor(chessBoard)
                saveBoardBetweenMoves()

            }

            override fun onArrangementMade(chessBoard: ChessBoard)
            {
                setActiveColor(chessBoard)
            }

            override fun onGameEnded(isWinSideWhite: Boolean)
            {
                TODO("Not yet implemented")
            }
        }


        return rootView
    }

    open fun onCreate() {}
    abstract fun loadBoard(savedInstanceState: Bundle?)
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