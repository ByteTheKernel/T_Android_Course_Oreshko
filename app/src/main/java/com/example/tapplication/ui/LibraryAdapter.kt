package com.example.tapplication.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.tapplication.databinding.ItemLibraryBinding
import com.example.tapplication.library.LibraryItem

class LibraryAdapter(
    private  val  onItemClick: (LibraryItem) -> Unit
): ListAdapter<LibraryItem, LibraryViewHolder>(LibraryDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LibraryViewHolder {
        val binding = ItemLibraryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LibraryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: LibraryViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position), onItemClick)
    }

    fun removeItem(position: Int) {
        val current = currentList.toMutableList()
        current.removeAt(position)
        submitList(current)
    }
}