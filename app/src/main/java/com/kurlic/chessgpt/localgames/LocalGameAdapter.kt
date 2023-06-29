package com.kurlic.chessgpt.localgames

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kurlic.chessgpt.R

class LocalGameAdapter : ListAdapter<LocalGame, LocalGameAdapter.LocalGameViewHolder>(LocalGameDiffCallback) {

    class LocalGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gameName: TextView = itemView.findViewById(R.id.gameName)
        // Другие поля
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalGameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.localgame_item, parent, false)
        return LocalGameViewHolder(view)
    }

    override fun onBindViewHolder(holder: LocalGameViewHolder, position: Int) {
        val game = getItem(position)
        holder.gameName.text = game.name
        // Установите значения других полей
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

