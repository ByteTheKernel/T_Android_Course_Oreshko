package com.example.tapplication.ui

import androidx.recyclerview.widget.RecyclerView
import com.example.tapplication.R
import com.example.tapplication.databinding.ItemLibraryBinding
import com.example.tapplication.library.*
import com.example.tapplication.library.LibraryItem

class LibraryViewHolder(private val binding: ItemLibraryBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: LibraryItem, onItemClick: (LibraryItem) -> Unit) {
        binding.itemName.text = item.name
        binding.itemId.text = itemView.context.getString(R.string.item_id, item.id)

        setIcon(item)

        setStyle(item, onItemClick)

        itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    private fun setIcon(item: LibraryItem) {
        binding.itemIcon.setImageResource(
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
        binding.itemName.alpha = alphaValue
        binding.itemId.alpha = alphaValue
        binding.itemCard.cardElevation = if (item.isAvailable) 10f else 1f
    }
}