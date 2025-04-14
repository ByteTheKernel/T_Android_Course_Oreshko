package com.example.tapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tapplication.databinding.ActivityDetailsBinding
import com.example.tapplication.library.*
import com.example.tapplication.ui.viewmodels.DetailsViewModel
import com.example.tapplication.utils.*

class DetailsActivity: AppCompatActivity() {

    companion object {
        const val EXTRA_ITEM = "extra_item"
        const val EXTRA_IS_EDIT_MODE = "extra_is_edit_mode"
        const val EXTRA_NEW_ITEM_TYPE = "extra_new_item_type"
    }

    private lateinit var binding: ActivityDetailsBinding
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isEditMode = intent.getBooleanExtra(EXTRA_IS_EDIT_MODE, false)

        viewModel.currentItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_ITEM, LibraryItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_ITEM) as? LibraryItem
        }

        if (viewModel.currentItem != null) {
            showItemDetails(viewModel.currentItem!!)
        }

        if (viewModel.isEditMode) {
            enableEditMode()
        } else {
            disableEditMode()
        }

        viewModel.newItemType = intent.getStringExtra(EXTRA_NEW_ITEM_TYPE)?.let {
            ItemType.valueOf(it)
        }

        if (viewModel.currentItem == null && viewModel.isEditMode && viewModel.newItemType != null ) {
            showEmptyFormForType(viewModel.newItemType!!)
        }

        binding.saveButton.setOnClickListener {
            if (viewModel.isEditMode && viewModel.newItemType != null) {
                val name = binding.itemDetailName.text.toString()
                val id = binding.itemId.text.toString().toIntOrNull() ?: 0
                val field1 = binding.libraryItemOptionalAttributeInput1.text.toString()
                val field2 = binding.libraryItemOptionalAttributeInput2.text.toString()

                val newItem = viewModel.createItemFromInput(
                    viewModel.newItemType!!,
                    name,
                    id,
                    field1,
                    field2
                )

                newItem?.let {
                    val resultIntent = Intent().apply {
                        putExtra(EXTRA_ITEM, it)
                    }
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
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
                binding.libraryItemOptionalAttributeLabel1.show()
                binding.libraryItemOptionalAttributeInput1.show()
                binding.libraryItemOptionalAttributeLabel1.text = getString(R.string.book_optional_attribute_label_1)
                binding.libraryItemOptionalAttributeInput1.setText(item.author)

                binding.libraryItemOptionalAttributeLabel2.show()
                binding.libraryItemOptionalAttributeInput2.show()
                binding.libraryItemOptionalAttributeLabel2.text = getString(R.string.book_optional_attribute_label_2)
                binding.libraryItemOptionalAttributeInput2.setText(item.pages.toString())
            }

            is Disk -> {
                binding.itemDetailIcon.setImageResource(R.drawable.disk_svg)
                binding.libraryItemOptionalAttributeLabel1.show()
                binding.libraryItemOptionalAttributeInput1.show()
                binding.libraryItemOptionalAttributeLabel1.text = getString(R.string.disk_optional_attribute_label_1)
                binding.libraryItemOptionalAttributeInput1.setText(item.type)

                binding.libraryItemOptionalAttributeLabel2.gone()
                binding.libraryItemOptionalAttributeInput2.gone()
            }

            is Newspaper -> {
                binding.itemDetailIcon.setImageResource(R.drawable.newspaper_svg)
                binding.libraryItemOptionalAttributeLabel1.show()
                binding.libraryItemOptionalAttributeInput1.show()
                binding.libraryItemOptionalAttributeLabel1.text = getString(R.string.newspaper_optional_attribute_label_1)
                binding.libraryItemOptionalAttributeInput1.setText(item.issueNumber.toString())

                binding.libraryItemOptionalAttributeLabel2.show()
                binding.libraryItemOptionalAttributeInput2.show()
                binding.libraryItemOptionalAttributeLabel2.text = getString(R.string.newspaper_optional_attribute_label_2)
                binding.libraryItemOptionalAttributeInput2.setText(item.month.russianMonth)
            }
        }
    }

    private fun enableEditMode() {
        binding.itemDetailName.isEnabled = true
        binding.libraryItemOptionalAttributeInput1.isEnabled = true
        binding.libraryItemOptionalAttributeInput2.isEnabled = true
        binding.saveButton.show()
    }

    private fun disableEditMode() {
        binding.itemDetailName.isEnabled = false
        binding.libraryItemOptionalAttributeInput1.isEnabled = false
        binding.libraryItemOptionalAttributeInput2.isEnabled = false
        binding.saveButton.gone()
    }

    private fun showEmptyFormForType(type: ItemType) {
        when(type) {
            ItemType.BOOK -> {
                binding.itemDetailIcon.setImageResource(R.drawable.book_svg)
                binding.libraryItemOptionalAttributeLabel1.text = getString(R.string.book_optional_attribute_label_1)
                binding.libraryItemOptionalAttributeLabel2.text = getString(R.string.book_optional_attribute_label_2)
                binding.libraryItemOptionalAttributeLabel1.show()
                binding.libraryItemOptionalAttributeLabel2.show()
                binding.libraryItemOptionalAttributeInput1.show()
                binding.libraryItemOptionalAttributeInput2.show()
            }

            ItemType.DISK -> {
                binding.itemDetailIcon.setImageResource(R.drawable.disk_svg)
                binding.libraryItemOptionalAttributeLabel1.text = getString(R.string.disk_optional_attribute_label_1)
                binding.libraryItemOptionalAttributeLabel1.show()
                binding.libraryItemOptionalAttributeInput1.show()
                binding.libraryItemOptionalAttributeLabel2.gone()
                binding.libraryItemOptionalAttributeInput2.gone()
            }

            ItemType.NEWSPAPER -> {
                binding.itemDetailIcon.setImageResource(R.drawable.newspaper_svg)
                binding.libraryItemOptionalAttributeLabel1.text = getString(R.string.newspaper_optional_attribute_label_1)
                binding.libraryItemOptionalAttributeLabel2.text = getString(R.string.newspaper_optional_attribute_label_2)
                binding.libraryItemOptionalAttributeLabel1.show()
                binding.libraryItemOptionalAttributeLabel2.show()
                binding.libraryItemOptionalAttributeInput1.show()
                binding.libraryItemOptionalAttributeInput2.show()
            }
        }
        binding.availableCheckbox.isChecked = true
        binding.availableCheckbox.isEnabled = false
    }
}