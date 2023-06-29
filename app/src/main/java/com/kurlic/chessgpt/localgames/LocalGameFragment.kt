package com.kurlic.chessgpt.localgames

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kurlic.chessgpt.R

class LocalGameFragment : Fragment()
{
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val rootView: View = inflater.inflate(R.layout.localgame_fragment, container, false)

        val recyclerView = rootView.findViewById<RecyclerView>(R.id.my_recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(context)

        val factory = LocalGameViewModelFactory(requireActivity().application)
        val viewModel = ViewModelProvider(this, factory).get(LocalGameViewModel::class.java)
        viewModel.allGames.observe(viewLifecycleOwner, { games ->
            // Update the cached copy of the games in the adapter.
            games?.let { (recyclerView.adapter as LocalGameAdapter).submitList(it) }
        })

        recyclerView.adapter = LocalGameAdapter()
        return rootView
    }
}