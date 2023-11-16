package com.kurlic.chessgpt.localgames

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kurlic.chessgpt.game.GameFragment
import com.kurlic.chessgpt.MainActivity
import com.kurlic.chessgpt.R
import kotlinx.coroutines.launch

class LocalGameAdapter : ListAdapter<LocalGame, LocalGameAdapter.LocalGameViewHolder>(LocalGameDiffCallback) {

    class LocalGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gameName: TextView = itemView.findViewById(R.id.localGameName)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteGame)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalGameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.localgame_item, parent, false)
        return LocalGameViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocalGameViewHolder, position: Int) {
        val game = getItem(position)
        holder.gameName.text = game.name

        holder.itemView.setOnClickListener {
            val gameId = game.id
            val bundle = Bundle()
            if (gameId != null) {
                bundle.putInt(GameFragment.ID_KEY, gameId.toInt())
                it.findNavController().navigate(R.id.action_LocalGameFragment_to_GameFragment, bundle)
            }

        }

        holder.deleteButton.setOnClickListener {
            val id: Int? = game.id;

            if (it.context is MainActivity) {
                val dao = (it.context as MainActivity).localGameDao;

                (it.context as MainActivity).lifecycleScope.launch {
                    dao?.deleteById(id!!)
                }
            }
        }
    }

    object LocalGameDiffCallback : DiffUtil.ItemCallback<LocalGame>() {
        override fun areItemsTheSame(oldItem: LocalGame, newItem: LocalGame): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LocalGame, newItem: LocalGame): Boolean {
            return oldItem == newItem
        }
    }
}

