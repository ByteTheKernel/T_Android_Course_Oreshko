package com.example.tapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tapplication.library.Book
import com.example.tapplication.library.Disk
import com.example.tapplication.library.LibraryItem
import com.example.tapplication.library.Newspaper
import com.example.tapplication.ui.LibraryAdapter
import com.example.tapplication.ui.SwipeToDeleteCallback
import com.example.tapplication.utils.Month

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: LibraryAdapter
    private lateinit var recyclerView: RecyclerView

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
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = LibraryAdapter(libraryItems) { item ->
            item.isAvailable = !item.isAvailable
            Toast.makeText(this, "Элемент с id #${item.id}", Toast.LENGTH_SHORT).show()
            val index = libraryItems.indexOf(item)
            if (index != -1) recyclerView.adapter?.notifyItemChanged(index)
        }

        recyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}