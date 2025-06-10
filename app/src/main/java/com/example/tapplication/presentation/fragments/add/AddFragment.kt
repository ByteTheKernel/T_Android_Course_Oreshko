package com.example.tapplication.presentation.fragments.add

import com.example.tapplication.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tapplication.App
import com.example.tapplication.common.extensions.gone
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

        // Подписка на ошибки
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailsViewModel.errorMessage.collect { error ->
                    error?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        // Выбор типа элемента
        binding.itemTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedType = ItemType.entries[position]
                detailsViewModel.updateType(selectedType)
                // При смене типа очищаем поля
                binding.libraryItemOptionalAttributeInput1.setText("")
                binding.libraryItemOptionalAttributeInput2.setText("")
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Кнопка сохранить
        binding.saveButton.setOnClickListener {
            detailsViewModel.createItemFromInputDirectly(
                name = binding.itemDetailName.text.toString(),
                id = binding.itemId.text.toString().toIntOrNull() ?: 0,
                field1 = binding.libraryItemOptionalAttributeInput1.text.toString(),
                field2 = binding.libraryItemOptionalAttributeInput2.text.toString()
            )
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
                binding.libraryItemOptionalAttributeLabel2.gone()
            }
            ItemType.NEWSPAPER -> {
                binding.itemDetailIcon.setImageResource(R.drawable.newspaper_svg)
                binding.libraryItemOptionalAttributeLabel1.hint = "Issue Number"
                binding.libraryItemOptionalAttributeLabel2.hint = "Month"
                binding.libraryItemOptionalAttributeLabel2.show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}