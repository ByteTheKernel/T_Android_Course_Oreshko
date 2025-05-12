package com.example.tapplication.presentation.fragments.list

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.example.tapplication.domain.entities.LibraryItem

class LibraryDiffCallback: DiffUtil.ItemCallback<LibraryItem>() {

    override fun areItemsTheSame(oldItem: LibraryItem, newItem: LibraryItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: LibraryItem, newItem: LibraryItem): Boolean {
        return oldItem == newItem
    }
}