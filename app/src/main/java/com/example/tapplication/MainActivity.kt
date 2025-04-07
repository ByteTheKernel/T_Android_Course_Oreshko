package com.example.tapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tapplication.databinding.ActivityMainBinding
import com.example.tapplication.library.Book
import com.example.tapplication.library.Disk
import com.example.tapplication.library.LibraryItem
import com.example.tapplication.library.Newspaper
import com.example.tapplication.ui.LibraryAdapter
import com.example.tapplication.ui.SwipeToDeleteCallback
import com.example.tapplication.utils.Month

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: LibraryAdapter

    private val libraryItems = mutableListOf<LibraryItem>(
        Book(101, true, "Мастер и Маргарита", 500, "М. Булгаков"),
        Book(102, true, "Преступление и наказание", 672, "Ф. Достоевский"),
        Newspaper(201, true, "Коммерсант", 789, Month.MARCH),
        Newspaper(202, true, "Известия", 1023, Month.FEBRUARY),
        Disk(301, true, "Интерстеллар", "DVD"),
        Disk(302, true, "Пинк Флойд - The Wall", "CD")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = LibraryAdapter { item ->
            updateAvailability(item)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        adapter.submitList(libraryItems.toList())

        val itemTouchHelper = ItemTouchHelper(
            SwipeToDeleteCallback { position ->
                libraryItems.removeAt(position)
                adapter.submitList(libraryItems.toList())
            })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun updateAvailability(item: LibraryItem) {
        val index = libraryItems.indexOfFirst { it.id == item.id }
        if (index != -1) {
            val updatedItem = when (item) {
                is Book -> item.copy(isAvailable = !item.isAvailable)
                is Disk -> item.copy(isAvailable = !item.isAvailable)
                is Newspaper -> item.copy(isAvailable = !item.isAvailable)
                else -> item
            }
            libraryItems[index] = updatedItem
            adapter.submitList(libraryItems.toList())
            Toast.makeText(this, "Элемент с id #${item.id}", Toast.LENGTH_SHORT).show()
        }
    }

}