package com.kurlic.chessgpt

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.kurlic.chessgpt.chess.ChessView
import com.kurlic.chessgpt.localgames.LocalGame
import com.kurlic.chessgpt.localgames.LocalGameDao
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
            localGameDao = (context as MainActivity).localGameDao!!
        }
    }

    lateinit var chessView: ChessView
    lateinit var gameNameTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val rootView: View = inflater.inflate(R.layout.game_fragment, container, false)

        gameId = requireArguments().getInt(ID_KEY)

        gameNameTextView = rootView.findViewById(R.id.gameName)

        val obj = localGameDao.getById(gameId)

        obj.observe(viewLifecycleOwner) { localGame ->
            localGame?.let {
                val text = localGame.name
                gameNameTextView.text = text

                val data = localGame.gameData
                chessView.loadBoardFromJson(data)
            }
        }

        chessView = rootView.findViewById(R.id.chessView)

        return rootView
    }

    override fun onDestroyView()
    {

        val jsonString: String = chessView.saveBoardToJson()

        val updatedGame = LocalGame(gameId, gameNameTextView.text.toString(), jsonString)
        lifecycleScope.launch {
            localGameDao.update(updatedGame)
        }



        super.onDestroyView()
    }
}