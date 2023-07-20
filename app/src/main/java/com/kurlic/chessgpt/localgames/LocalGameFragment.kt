package com.kurlic.chessgpt.localgames

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.kurlic.chessgpt.game.GameFragment
import kotlinx.coroutines.launch

class LocalGameFragment : GameFragment()
{

    override fun loadBoard(savedInstanceState: Bundle?)
    {
        if(savedInstanceState != null)
        {
            gameId = savedInstanceState.getInt(ID_KEY, -1);
        }
        if(gameId == -1)
        {
            gameId = requireArguments().getInt(ID_KEY)
        }

        getDataFromDao()
    }


    private fun getDataFromDao()
    {
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
    override fun saveBoardBetweenMoves()
    {
        val jsonString: String = chessView.saveBoardToJson()

        val updatedGame = LocalGame(gameId, gameNameTextView.text.toString(), jsonString)
        lifecycleScope.launch {
            localGameDao.update(updatedGame)
        }
    }

    override fun saveBoardOnDestroyView(outState: Bundle)
    {
        outState.putInt(ID_KEY, gameId)
    }

}