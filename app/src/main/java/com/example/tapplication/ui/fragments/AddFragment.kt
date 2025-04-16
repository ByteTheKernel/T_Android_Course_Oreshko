package com.example.tapplication.ui.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tapplication.MainActivity
import com.example.tapplication.R
import com.example.tapplication.databinding.FragmentAddBinding
import com.example.tapplication.library.LibraryItem
import com.example.tapplication.ui.viewmodels.DetailsViewModel
import com.example.tapplication.ui.viewmodels.MainViewModel
import com.example.tapplication.utils.ItemType


class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val detailsViewModel: DetailsViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        // Настройка Spinner для выбора типа элемента
        val itemTypes = ItemType.entries.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, itemTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.itemTypeSpinner.adapter = adapter

        // Обработка выбора типа элемента
        binding.itemTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedType = ItemType.entries[position]
                updateUIForType(selectedType)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.saveButton.setOnClickListener {
            val newItem = createItemFromInput()
            if (newItem != null) {
                mainViewModel.addItem(newItem)

                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    findNavController().navigateUp()
                } else {
                    (activity as MainActivity).hideDetail()
                }
            } else {
                showError("Please fill all fields correctly")
            }
        }

        return binding.root
    }

    private fun updateUIForType(type: ItemType) {
        when (type) {
            ItemType.BOOK -> {
                binding.itemDetailIcon.setImageResource(R.drawable.book_svg)
                binding.libraryItemOptionalAttributeLabel1.hint = "Author"
                binding.libraryItemOptionalAttributeLabel2.hint = "Pages"
                binding.libraryItemOptionalAttributeLabel2.visibility = View.VISIBLE
            }
            ItemType.DISK -> {
                binding.itemDetailIcon.setImageResource(R.drawable.disk_svg)
                binding.libraryItemOptionalAttributeLabel1.hint = "Type"
                binding.libraryItemOptionalAttributeLabel2.visibility = View.GONE
            }
            ItemType.NEWSPAPER -> {
                binding.itemDetailIcon.setImageResource(R.drawable.newspaper_svg)
                binding.libraryItemOptionalAttributeLabel1.hint = "Issue Number"
                binding.libraryItemOptionalAttributeLabel2.hint = "Month"
                binding.libraryItemOptionalAttributeLabel2.visibility = View.VISIBLE
            }
        }
    }

    private fun createItemFromInput(): LibraryItem? {
        val type = ItemType.valueOf(binding.itemTypeSpinner.selectedItem.toString())
        val name = binding.itemDetailName.text.toString()
        val id = binding.itemId.text.toString().toIntOrNull() ?: return null
        val field1 = binding.libraryItemOptionalAttributeInput1.text.toString()
        val field2 = binding.libraryItemOptionalAttributeInput2.text.toString()

        return detailsViewModel.createItemFromInput(type, name, id, field1, field2)
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}