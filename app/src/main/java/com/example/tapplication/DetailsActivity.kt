package com.example.tapplication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tapplication.databinding.ActivityDetailsBinding
import com.example.tapplication.library.*
import com.example.tapplication.utils.*

class DetailsActivity: AppCompatActivity() {

    companion object {
        const val EXTRA_ITEM = "extra_item"
        const val EXTRA_IS_EDIT_MODE = "extra_is_edit_mode"
        const val EXTRA_NEW_ITEM_TYPE = "extra_new_item_type"
    }

    private lateinit var binding: ActivityDetailsBinding
    private var isEditMode = false
    private var item: LibraryItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isEditMode = intent.getBooleanExtra(EXTRA_IS_EDIT_MODE, false)
        item = intent.getSerializableExtra(EXTRA_ITEM) as? LibraryItem

        if (item != null) {
            showItemDetails(item!!)
        }

        if (isEditMode) {
            enableEditMode()
        } else {
            disableEditMode()
        }

        val newType = intent.getStringExtra(EXTRA_NEW_ITEM_TYPE)
        if (item == null && isEditMode && newType != null ) {
            showEmptyFormForType(ItemType.valueOf(newType))
        }

        binding.saveButton.setOnClickListener {
            if (isEditMode && newType != null) {
                val newItem = createItemFromInput(ItemType.valueOf(newType))
                setResult(RESULT_OK, intent.putExtra(EXTRA_ITEM, newItem))
                finish()
            }
        }
    }

    private fun showItemDetails(item: LibraryItem) {

        binding.itemDetailName.setText(item.name)
        binding.availableCheckbox.isChecked = item.isAvailable
        binding.availableCheckbox.isEnabled = false
        binding.itemId.setText(item.id.toString())
        binding.itemId.isEnabled = false

        when (item) {
            is Book -> {
                binding.itemDetailIcon.setImageResource(R.drawable.book_svg)
                binding.extraLabel1.visibility = View.VISIBLE
                binding.extraInput1.visibility = View.VISIBLE
                binding.extraLabel1.text = "Автор"
                binding.extraInput1.setText(item.author)

                binding.extraLabel2.visibility = View.VISIBLE
                binding.extraInput2.visibility = View.VISIBLE
                binding.extraLabel2.text = "Страниц"
                binding.extraInput2.setText(item.pages.toString())
            }

            is Disk -> {
                binding.itemDetailIcon.setImageResource(R.drawable.disk_svg)
                binding.extraLabel1.visibility = View.VISIBLE
                binding.extraInput1.visibility = View.VISIBLE
                binding.extraLabel1.text = "Тип диска"
                binding.extraInput1.setText(item.type)

                binding.extraLabel2.visibility = View.GONE
                binding.extraInput2.visibility = View.GONE
            }

            is Newspaper -> {
                binding.itemDetailIcon.setImageResource(R.drawable.newspaper_svg)
                binding.extraLabel1.visibility = View.VISIBLE
                binding.extraInput1.visibility = View.VISIBLE
                binding.extraLabel1.text = "Выпуск"
                binding.extraInput1.setText(item.issueNumber.toString())

                binding.extraLabel2.visibility = View.VISIBLE
                binding.extraInput2.visibility = View.VISIBLE
                binding.extraLabel2.text = "Месяц"
                binding.extraInput2.setText(item.month.russianMonth)
            }
        }
    }

    private fun enableEditMode() {
        binding.itemDetailName.isEnabled = true
        binding.extraInput1.isEnabled = true
        binding.extraInput2.isEnabled = true
        binding.saveButton.visibility = View.VISIBLE
    }

    private fun disableEditMode() {
        binding.itemDetailName.isEnabled = false
        binding.extraInput1.isEnabled = false
        binding.extraInput2.isEnabled = false
        binding.saveButton.visibility = View.GONE
    }

    private fun showEmptyFormForType(type: ItemType) {
        when(type) {
            ItemType.BOOK -> {
                binding.itemDetailIcon.setImageResource(R.drawable.book_svg)
                binding.extraLabel1.text = "Автор"
                binding.extraLabel2.text = "Страниц"
                binding.extraLabel1.visibility = View.VISIBLE
                binding.extraLabel2.visibility = View.VISIBLE
                binding.extraInput1.visibility = View.VISIBLE
                binding.extraInput2.visibility = View.VISIBLE
            }

            ItemType.DISK -> {
                binding.itemDetailIcon.setImageResource(R.drawable.disk_svg)
                binding.extraLabel1.text = "Тип диска"
                binding.extraLabel1.visibility = View.VISIBLE
                binding.extraInput1.visibility = View.VISIBLE
                binding.extraLabel2.visibility = View.GONE
                binding.extraInput2.visibility = View.GONE
            }

            ItemType.NEWSPAPER -> {
                binding.itemDetailIcon.setImageResource(R.drawable.newspaper_svg)
                binding.extraLabel1.text = "Выпуск"
                binding.extraLabel2.text = "Месяц"
                binding.extraLabel1.visibility = View.VISIBLE
                binding.extraLabel2.visibility = View.VISIBLE
                binding.extraInput1.visibility = View.VISIBLE
                binding.extraInput2.visibility = View.VISIBLE
            }
        }
        binding.availableCheckbox.isChecked = true
        binding.availableCheckbox.isEnabled = false
    }

    private fun createItemFromInput(type: ItemType): LibraryItem? {
        val name = binding.itemDetailName.text.toString()
        val id = binding.itemId.text.toString().toIntOrNull() ?: 0
        val field1 = binding.extraInput1.text.toString()
        val field2 = binding.extraInput2.text.toString()

        return when (type) {
            ItemType.BOOK -> {
                Book(
                    id = id,
                    isAvailable = true,
                    name = name,
                    pages = field2.toIntOrNull() ?: 0,
                    author = field1
                )
            }

            ItemType.DISK -> {
                Disk(
                    id = id,
                    isAvailable = true,
                    name = name,
                    type = field1
                )
            }

            ItemType.NEWSPAPER -> {
                val month = Month.entries.firstOrNull {
                    it.russianMonth.equals(
                        field2, ignoreCase = true
                    )} ?: return null

                Newspaper(
                    id = id,
                    isAvailable = true,
                    name = name,
                    issueNumber = field1.toIntOrNull() ?: 0,
                    month = month
                )
            }
        }
    }
}