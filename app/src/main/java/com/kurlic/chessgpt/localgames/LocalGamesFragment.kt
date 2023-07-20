package com.kurlic.chessgpt.localgames

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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

    fun setNewGameButton(rootView: View)
    {
        newGame = rootView.findViewById(R.id.localGameNewGame)

        newGame.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val inflater = layoutInflater
            val view = inflater.inflate(R.layout.localgamecreate_view, null)


            val alertDialog = builder.setView(view)
            alertDialog.setPositiveButton("OK")
            { dialog, id ->
                val name = view.findViewById<EditText>(R.id.editGameName).text.toString();
                val game = LocalGame(null, name, null)
                lifecycleScope.launch {
                    val num = viewModel.localGameDao.insert(game)
                    //Toast.makeText(context, num.toString(), Toast.LENGTH_SHORT).show()

                    val bundle = Bundle()
                    bundle.putInt(GameFragment.ID_KEY, num.toInt())
                    findNavController().navigate(R.id.action_LocalGameFragment_to_GameFragment, bundle)
                }


            }

            alertDialog.setNegativeButton("Cancel")
            { dialog, id ->
            }



            val alert = alertDialog.create()
            .apply {
                show()
            }

            alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.textColor))
            alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.textColor))

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