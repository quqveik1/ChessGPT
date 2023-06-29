package com.kurlic.chessgpt.localgames

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kurlic.chessgpt.GameFragment
import com.kurlic.chessgpt.R

class LocalGameAdapter : ListAdapter<LocalGame, LocalGameAdapter.LocalGameViewHolder>(LocalGameDiffCallback) {

    class LocalGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gameName: TextView = itemView.findViewById(R.id.localGameName)
        // Другие поля
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalGameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.localgame_item, parent, false)
        return LocalGameViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocalGameViewHolder, position: Int) {
        val game = getItem(position)
        holder.gameName.text = game.name

        holder.itemView.setOnClickListener{
            val gameId = game.id
            val bundle = Bundle()
            if (gameId != null)
            {
                bundle.putInt(GameFragment.ID_KEY, gameId.toInt())
                it.findNavController().navigate(R.id.action_LocalGameFragment_to_GameFragment, bundle)
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

