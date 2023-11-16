package com.kurlic.chessgpt.game

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.kurlic.chessgpt.MainActivity
import com.kurlic.chessgpt.R
import com.kurlic.chessgpt.chess.ChessBoard
import com.kurlic.chessgpt.chess.ChessMoveListener
import com.kurlic.chessgpt.chess.ChessView
import com.kurlic.chessgpt.localgames.LocalGameDao
import com.kurlic.chessgpt.localgames.LocalGameDataBase

abstract class GameFragment : Fragment() {
    companion object {
        const val ID_KEY = "id"
        const val BOTTOMSIDE_KEY = "bottom_side"
    }

    var gameId: Int = -1;

    lateinit var localGameDao: LocalGameDao

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            localGameDao = LocalGameDataBase.getDatabase(context).localGameDao()
        }
    }

    lateinit var chessView: ChessView
    lateinit var gameNameTextView: TextView
    lateinit var activeMoveSideTextView: TextView

    var isBottomSideWhite: Boolean? = null

    private val whiteMove: String by lazy {
        requireContext().getString(R.string.white_move)
    }
    private val blackMove: String by lazy {
        requireContext().getString(R.string.black_move)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView: View = inflater.inflate(R.layout.game_fragment, container, false)

        gameNameTextView = rootView.findViewById(R.id.gameName)
        chessView = rootView.findViewById(R.id.chessView)
        activeMoveSideTextView = rootView.findViewById(R.id.activeMoveSide)

        chessView.moveListener = getChessMoveListener()

        isBottomSideWhite = arguments?.getBoolean(BOTTOMSIDE_KEY)

        onCreate()

        loadBoard(savedInstanceState)

        return rootView
    }

    open inner class GameMoveListener : ChessMoveListener {
        override fun onMoveMade(chessBoard: ChessBoard) {
            setActiveColor(chessBoard)
            saveBoardBetweenMoves()

        }

        override fun onArrangementMade(chessBoard: ChessBoard) {
            setActiveColor(chessBoard)
        }

        override fun onGameEnded(isWinSideWhite: Boolean) {

            val winner = if (isWinSideWhite) requireContext().getString(R.string.white) else requireContext().getString(R.string.black)

            val alertDialog = AlertDialog.Builder(requireContext()).apply {
                setTitle(requireContext().getString(R.string.game_ended))
                setMessage("$winner ${requireContext().getString(R.string.congratulate_for_side)}")
                setPositiveButton("Ok") { _, _ ->
                    parentFragmentManager.popBackStack()
                }.setOnCancelListener { parentFragmentManager.popBackStack() }
            }

            val alert = alertDialog.create().apply {
                show()
            }

            alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.textColor))
        }
    }

    open fun onCreate() {
    }

    abstract fun loadBoard(savedInstanceState: Bundle?)
    open fun getChessMoveListener(): GameMoveListener? {
        return GameMoveListener()
    }

    open fun saveBoardBetweenMoves() {
    }

    abstract fun saveBoardOnDestroyView(outState: Bundle)

    fun setActiveColor(chessBoard: ChessBoard) {
        if (chessBoard.isActiveSideWhite) {
            activeMoveSideTextView.text = whiteMove
        } else {
            activeMoveSideTextView.text = blackMove
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        saveBoardOnDestroyView(outState)
    }
}