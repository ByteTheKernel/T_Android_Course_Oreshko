package com.example.tapplication

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tapplication.databinding.ActivityMainBinding
import com.example.tapplication.library.Book
import com.example.tapplication.library.Disk
import com.example.tapplication.library.LibraryItem
import com.example.tapplication.library.Newspaper
import com.example.tapplication.ui.LibraryAdapter
import com.example.tapplication.ui.SwipeToDeleteCallback
import com.example.tapplication.ui.viewmodels.MainViewModel
import com.example.tapplication.utils.*

class MainActivity : AppCompatActivity() {
    companion object {
        private const val ADD_ITEM_REQUEST_CODE = 100
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: LibraryAdapter
    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        adapter = LibraryAdapter(
            onItemClick = { item -> openDetails(item) },
            onItemLongClick = { item -> viewModel.updateItemAvailability(item) }
        )


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter


        viewModel.items.observe(this) { items ->
            adapter.submitList(items)
        }

        viewModel.toastMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }

        val itemTouchHelper = ItemTouchHelper(
            SwipeToDeleteCallback { position ->
                viewModel.removeItem(position)
            })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionAdd -> {
                openDetailsForNewItem()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_ITEM_REQUEST_CODE && resultCode == RESULT_OK) {
            val newItem = data?.getParcelableExtra<LibraryItem>(DetailsActivity.EXTRA_ITEM)
            newItem?.let { viewModel.addItem(it) }

        }
    }

    private fun openDetails(item: LibraryItem) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.EXTRA_ITEM, item as Parcelable)
        intent.putExtra(DetailsActivity.EXTRA_IS_EDIT_MODE, false)
        startActivity(intent)
    }

    private fun openDetailsForNewItem() {
        val options = arrayOf("Книга", "Диск", "Газета")
        AlertDialog.Builder(this)
            .setTitle("Выберите тип элемента")
            .setItems(options) { _, which ->
                val selectedItemType = when (which) {
                    0 -> ItemType.BOOK
                    1 -> ItemType.DISK
                    2 -> ItemType.NEWSPAPER
                    else -> null
                }
                selectedItemType?.let {
                    val intent = Intent(this, DetailsActivity::class.java)
                    intent.putExtra(DetailsActivity.EXTRA_IS_EDIT_MODE, true)
                    intent.putExtra(DetailsActivity.EXTRA_NEW_ITEM_TYPE, selectedItemType.name)
                    startActivityForResult(intent, ADD_ITEM_REQUEST_CODE)
                }
            }
            .show()
    }
}