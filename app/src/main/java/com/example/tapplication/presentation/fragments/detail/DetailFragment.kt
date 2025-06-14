package com.example.tapplication.presentation.fragments.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.tapplication.App
import com.example.tapplication.R
import com.example.tapplication.common.extensions.gone
import com.example.tapplication.databinding.FragmentDetailBinding
import com.example.tapplication.domain.entities.Book
import com.example.tapplication.domain.entities.Disk
import com.example.tapplication.domain.entities.LibraryItem
import com.example.tapplication.domain.entities.Newspaper
import com.example.tapplication.presentation.MainActivity
import com.example.tapplication.presentation.viewmodels.MainViewModel

class DetailFragment : Fragment() {

    private val args: DetailFragmentArgs by navArgs()

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels {
        (requireActivity().application as App).presentationComponent.daggerViewModelFactory()
    }
    val itemId: Int by lazy { args.itemId }
    val isTwoPane: Boolean by lazy { args.isTwoPane }

    private val presentationComponent by lazy {
        (requireActivity().application as App).presentationComponent
    }

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

        presentationComponent.inject(this)

        viewModel.getItemById(itemId)?.let { item ->
            bindItem(item)
        } ?: run {
            Toast.makeText(requireContext(), "Element not found", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    private fun bindItem(item: LibraryItem) {
        with(binding) {
            setupCommonFields(item)
            when (item) {
                is Book -> setupBookFields(item)
                is Disk -> setupDiskFields(item)
                is Newspaper -> setupNewspaperFields(item)
            }
        }
    }

    private fun setupCommonFields(item: LibraryItem) {
        with(binding) {
            itemDetailName.setText(item.name)
            itemId.setText(item.id.toString())
            availableCheckbox.isChecked = item.isAvailable
            availableCheckbox.isEnabled = false
            itemDetailName.isEnabled = false
            libraryItemOptionalAttributeInput1.isEnabled = false
            libraryItemOptionalAttributeInput2.isEnabled = false
            itemId.isEnabled = false
        }
    }

    private fun setupBookFields(book: Book) {
        with(binding) {
            itemDetailIcon.setImageResource(R.drawable.book_svg)
            libraryItemOptionalAttributeLabel1.hint = getString(R.string.book_optional_attribute_label_1)
            libraryItemOptionalAttributeInput1.setText(book.author)
            libraryItemOptionalAttributeLabel2.hint = getString(R.string.book_optional_attribute_label_2)
            libraryItemOptionalAttributeInput2.setText(book.pages.toString())

            val iconUrl = book.iconUrl

            if(!iconUrl.isNullOrEmpty()) {
                Glide.with(itemDetailIcon.context)
                    .load(iconUrl)
                    .placeholder(R.drawable.book_svg)
                    .error(R.drawable.book_svg)
                    .into(itemDetailIcon)
            } else {
                itemDetailIcon.setImageResource(R.drawable.book_svg)
            }
        }
    }

    private fun setupDiskFields(disk: Disk) {
        with(binding) {
            itemDetailIcon.setImageResource(R.drawable.disk_svg)
            libraryItemOptionalAttributeLabel1.hint = getString(R.string.disk_optional_attribute_label_1)
            libraryItemOptionalAttributeInput1.setText(disk.type)
            libraryItemOptionalAttributeLabel2.gone()
        }
    }

    private fun setupNewspaperFields(newspaper: Newspaper) {
        with(binding) {
            itemDetailIcon.setImageResource(R.drawable.newspaper_svg)
            libraryItemOptionalAttributeLabel1.hint = getString(R.string.newspaper_optional_attribute_label_1)
            libraryItemOptionalAttributeInput1.setText(newspaper.issueNumber.toString())
            libraryItemOptionalAttributeLabel2.hint = getString(R.string.newspaper_optional_attribute_label_2)
            libraryItemOptionalAttributeInput2.setText(newspaper.month.russianMonth)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}