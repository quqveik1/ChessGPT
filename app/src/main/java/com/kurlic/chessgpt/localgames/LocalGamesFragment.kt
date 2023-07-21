package com.kurlic.chessgpt.localgames

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kurlic.chessgpt.game.GameFragment
import com.kurlic.chessgpt.MainActivity
import com.kurlic.chessgpt.R
import com.kurlic.chessgpt.game.NewGameCreateDialog
import kotlinx.coroutines.launch

class LocalGamesFragment : Fragment()
{
    lateinit var newGame: Button
    lateinit var viewModel: LocalGameViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val rootView: View = inflater.inflate(R.layout.localgame_fragment, container, false)
        createLocalGamesDB(rootView)

        setNewGameButton(rootView)

        return rootView
    }

    inner class LocalGameCreateDialog : NewGameCreateDialog()
    {
        override fun onOk(bundle: Bundle, name: String)
        {
            super.onOk(bundle, name)

            lifecycleScope.launch {
                val game = LocalGame(null, name, null)
                val num = viewModel.localGameDao.insert(game)

                bundle.putInt(GameFragment.ID_KEY, num.toInt())

                findNavController().navigate(R.id.action_LocalGameFragment_to_GameFragment, bundle)
            }
        }
    }


    fun setNewGameButton(rootView: View)
    {
        newGame = rootView.findViewById(R.id.localGameNewGame)

        newGame.setOnClickListener {
            val dialog = LocalGameCreateDialog()

            dialog.show(requireContext(), layoutInflater)

        }
    }

    fun createLocalGamesDB(rootView: View)
    {
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.my_recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(context)

        val factory = LocalGameViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory).get(LocalGameViewModel::class.java)
        viewModel.allGames.observe(viewLifecycleOwner) { games ->
            // Update the cached copy of the games in the adapter.
            games?.let { (recyclerView.adapter as LocalGameAdapter).submitList(it) }
        }

        if(context is MainActivity)
        {
            (context as MainActivity).localGameDao = viewModel.localGameDao
        }

        recyclerView.adapter = LocalGameAdapter()
    }
}