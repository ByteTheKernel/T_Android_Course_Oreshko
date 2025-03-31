package com.example.tapplication.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.tapplication.R
import com.example.tapplication.library.*
import com.example.tapplication.library.LibraryItem

class LibraryViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val icon: ImageView = view.findViewById(R.id.item_icon)
    private val name: TextView = view.findViewById(R.id.item_name)
    private val id: TextView = view.findViewById(R.id.item_id)
    private val card: CardView = view as CardView

    fun bind(item: LibraryItem, onItemClick: (LibraryItem) -> Unit) {
        name.text = item.name
        id.text = itemView.context.getString(R.string.item_id, item.id)

        setIcon(item)

        setStyle(item, onItemClick)
    }

    private fun setIcon(item: LibraryItem) {
        icon.setImageResource(
            when(item) {
                is Book -> R.drawable.free_icon_book
                is Newspaper -> R.drawable.free_icon_newspaper
                is Disk -> R.drawable.free_icon_vinyl
                else -> R.drawable.unknown
            }
        )
    }

    private fun setStyle(item: LibraryItem, onItemClick: (LibraryItem) -> Unit) {
        val alphaValue = if (item.isAvailable) 1f else 0.3f
        name.alpha = alphaValue
        id.alpha = alphaValue
        card.cardElevation = if (item.isAvailable) 10f else 1f

        itemView.setOnClickListener {
            onItemClick(item)
        }
    }
}