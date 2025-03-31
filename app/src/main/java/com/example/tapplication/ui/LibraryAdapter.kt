package com.example.tapplication.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tapplication.R
import com.example.tapplication.library.LibraryItem

class LibraryAdapter(
    private val items: MutableList<LibraryItem>,
    private  val  onItemClick: (LibraryItem) -> Unit
): RecyclerView.Adapter<LibraryViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LibraryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_library, parent, false)
        return LibraryViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: LibraryViewHolder,
        position: Int
    ) {
        holder.bind(items[position], onItemClick)
    }

    override fun getItemCount(): Int = items.size

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}