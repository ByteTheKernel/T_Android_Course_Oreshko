package com.example.tapplication.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tapplication.R
import com.example.tapplication.databinding.FragmentDetailBinding
import com.example.tapplication.library.*
import com.example.tapplication.library.LibraryItem
import com.example.tapplication.ui.viewmodels.MainViewModel
import com.example.tapplication.utils.gone

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private val itemId by lazy { arguments?.getInt("itemId") ?: 0 }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemId = arguments?.getInt("itemId") ?: run {
            Log.e("DetailFragment", "itemId is null")
            return
        }

        viewModel.getItemById(itemId)?.let { item ->
            bindItem(item)
        } ?: run {
            Toast.makeText(requireContext(), "Element not found", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    private fun bindItem(item: LibraryItem) {
        with(binding) {
            itemDetailName.setText(item.name)
            itemId.setText(item.id.toString())
            availableCheckbox.isChecked = item.isAvailable
            availableCheckbox.isEnabled = false
            itemDetailName.isEnabled = false
            libraryItemOptionalAttributeInput1.isEnabled = false
            libraryItemOptionalAttributeInput2.isEnabled = false
            itemId.isEnabled = false

            when (item) {
                is Book -> {
                    itemDetailIcon.setImageResource(R.drawable.book_svg)
                    libraryItemOptionalAttributeLabel1.hint = getString(R.string.book_optional_attribute_label_1)
                    libraryItemOptionalAttributeInput1.setText(item.author)
                    libraryItemOptionalAttributeLabel2.hint = getString(R.string.book_optional_attribute_label_2)
                    libraryItemOptionalAttributeInput2.setText(item.pages.toString())
                }
                is Disk -> {
                    itemDetailIcon.setImageResource(R.drawable.disk_svg)
                    libraryItemOptionalAttributeLabel1.hint = getString(R.string.disk_optional_attribute_label_1)
                    libraryItemOptionalAttributeInput1.setText(item.type)
                    libraryItemOptionalAttributeLabel2.gone()
                }
                is Newspaper -> {
                    itemDetailIcon.setImageResource(R.drawable.newspaper_svg)
                    libraryItemOptionalAttributeLabel1.hint = getString(R.string.newspaper_optional_attribute_label_1)
                    libraryItemOptionalAttributeInput1.setText(item.issueNumber.toString())
                    libraryItemOptionalAttributeLabel2.hint = getString(R.string.newspaper_optional_attribute_label_1)
                    libraryItemOptionalAttributeInput2.setText(item.month.russianMonth)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}