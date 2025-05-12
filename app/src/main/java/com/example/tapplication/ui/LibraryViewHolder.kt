package com.example.tapplication.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
    }

    private fun setIcon(item: LibraryItem) {
        when (item) {
            is Book -> {
                val iconUrl = item.iconUrl
                val placeholder = R.drawable.book_svg

                if (!iconUrl.isNullOrEmpty()) {
                    Glide.with(binding.itemIcon.context)
                        .load(iconUrl)
                        .placeholder(placeholder)
                        .error(placeholder)
                        .into(binding.itemIcon)
                } else {
                    binding.itemIcon.setImageResource(placeholder)
                }
            }

            is Newspaper -> {
                binding.itemIcon.setImageResource(R.drawable.newspaper_svg)
            }

            is Disk -> {
                binding.itemIcon.setImageResource(R.drawable.disk_svg)
            }

            else -> {
                binding.itemIcon.setImageResource(R.drawable.unknown_svg)
            }
        }
    }

    private fun setStyle(item: LibraryItem, onItemClick: (LibraryItem) -> Unit) {
        val alphaValue = if (item.isAvailable) 1f else 0.3f
        binding.itemName.alpha = alphaValue
        binding.itemId.alpha = alphaValue
        binding.itemCard.cardElevation = if (item.isAvailable) 10f else 1f
    }
}