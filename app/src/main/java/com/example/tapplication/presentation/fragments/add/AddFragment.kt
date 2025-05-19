package com.example.tapplication.presentation.fragments.add

import com.example.tapplication.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tapplication.App
import com.example.tapplication.common.extensions.show
import com.example.tapplication.common.utils.ItemType
import com.example.tapplication.databinding.FragmentAddBinding
import com.example.tapplication.presentation.MainActivity
import com.example.tapplication.presentation.viewmodels.DetailsViewModel
import com.example.tapplication.presentation.viewmodels.MainViewModel
import kotlinx.coroutines.launch

class AddFragment : Fragment() {

    private val args: AddFragmentArgs by navArgs()

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val detailsViewModel: DetailsViewModel by viewModels{
        (requireActivity().application as App).presentationComponent.daggerViewModelFactory()
    }
    private val mainViewModel: MainViewModel by viewModels {
        (requireActivity().application as App).presentationComponent.daggerViewModelFactory()
    }
    private val isTwoPane: Boolean by lazy { args.isTwoPane }

    private val presentationComponent by lazy {
        (requireActivity().application as App).presentationComponent
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presentationComponent.inject(this)

        // Настройка Spinner для выбора типа элемента
        val itemTypes = ItemType.entries.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, itemTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.itemTypeSpinner.adapter = adapter

        // Подписываемся на изменения состояния формы
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailsViewModel.formState.collect { formSate ->
                    updateUIForType(formSate.type)
                }
            }
        }

        // Подписываемся на создание нового элемента
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailsViewModel.createdItem.collect { newItem ->
                    if (newItem != null) {
                        mainViewModel.addItem(newItem)
                        detailsViewModel.resetForm()

                        if (!isTwoPane) {
                            findNavController().navigateUp()
                        } else {
                            (activity as MainActivity).hideDetail()
                        }
                    }
                }
            }
        }

        // Обработка выбора типа элемента
        binding.itemTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedType = ItemType.entries[position]
                detailsViewModel.updateType(selectedType)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Обработка ввода данных
        binding.itemDetailName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                detailsViewModel.updateName(binding.itemDetailName.text.toString())
            }
        }

        binding.itemId.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                detailsViewModel.updateId(binding.itemId.text.toString().toIntOrNull() ?: 0)
            }
        }

        binding.libraryItemOptionalAttributeInput1.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                detailsViewModel.updateField1(binding.libraryItemOptionalAttributeInput1.text.toString())
            }
        }

        binding.libraryItemOptionalAttributeInput2.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                detailsViewModel.updateField2(binding.libraryItemOptionalAttributeInput2.text.toString())
            }
        }


        binding.saveButton.setOnClickListener {
            detailsViewModel.createItemFromInput()
        }

    }

    private fun updateUIForType(type: ItemType) {
        when (type) {
            ItemType.BOOK -> {
                binding.itemDetailIcon.setImageResource(R.drawable.book_svg)
                binding.libraryItemOptionalAttributeLabel1.hint = "Author"
                binding.libraryItemOptionalAttributeLabel2.hint = "Pages"
                binding.libraryItemOptionalAttributeLabel2.show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}