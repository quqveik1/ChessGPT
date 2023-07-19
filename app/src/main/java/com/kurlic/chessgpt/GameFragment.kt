package com.kurlic.chessgpt

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.kurlic.chessgpt.chess.ChessBoard
import com.kurlic.chessgpt.chess.ChessMoveListener
import com.kurlic.chessgpt.chess.ChessView
import com.kurlic.chessgpt.localgames.LocalGame
import com.kurlic.chessgpt.localgames.LocalGameDao
import com.kurlic.chessgpt.localgames.LocalGameDataBase
import kotlinx.coroutines.launch

class GameFragment:Fragment()
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

    private val whiteMove: String = "Ход белых"
    private val blackMove: String = "Ход чёрных"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        val rootView: View = inflater.inflate(R.layout.game_fragment, container, false)

        if(savedInstanceState != null)
        {
            gameId = savedInstanceState.getInt(ID_KEY, -1);
        }
        if(gameId == -1)
        {
            gameId = requireArguments().getInt(ID_KEY)
        }

        getDataFromDao(rootView);

        activeMoveSideTextView = rootView.findViewById(R.id.activeMoveSide)

        chessView.moveListener = object : ChessMoveListener {
            override fun onMoveMade(chessBoard: ChessBoard) {
                setActiveColor(chessBoard)
                saveBoard()

            }

            override fun onArrangementMade(chessBoard: ChessBoard)
            {
                setActiveColor(chessBoard)
            }
        }


        return rootView
    }

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

    fun getDataFromDao(rootView: View)
    {
        gameNameTextView = rootView.findViewById(R.id.gameName)
        chessView = rootView.findViewById(R.id.chessView)

        val obj = localGameDao.getById(gameId)

        obj.observe(viewLifecycleOwner) { localGame ->
            localGame?.let {
                val text = localGame.name
                gameNameTextView.text = text

                val data = localGame.gameData
                chessView.loadBoardFromJson(data)
            }
        }
    }

    fun saveBoard()
    {
        val jsonString: String = chessView.saveBoardToJson()

        val updatedGame = LocalGame(gameId, gameNameTextView.text.toString(), jsonString)
        lifecycleScope.launch {
            localGameDao.update(updatedGame)
        }
    }

    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)

        outState.putInt(ID_KEY, gameId)
    }
}