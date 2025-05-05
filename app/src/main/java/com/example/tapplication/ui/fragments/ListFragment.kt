package com.example.tapplication.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tapplication.MainActivity
import com.example.tapplication.R
import com.example.tapplication.databinding.FragmentListBinding
import com.example.tapplication.ui.LibraryAdapter
import com.example.tapplication.ui.SwipeToDeleteCallback
import com.example.tapplication.ui.viewmodels.MainViewModel
import com.example.tapplication.ui.viewmodels.MainViewModel.Tab
import com.example.tapplication.utils.SortOrder
import com.example.tapplication.utils.gone
import com.example.tapplication.utils.show

class ListFragment: Fragment() {
    companion object {
        fun getInstance(isTwoPane: Boolean): ListFragment {
            return ListFragment().apply {
                arguments = createBundle(isTwoPane)
            }
        }

        private fun createBundle(isTwoPane: Boolean): Bundle {
            return Bundle().apply {
                putBoolean("isTwoPane", isTwoPane)
            }
        }
    }

    private val viewModel: MainViewModel by activityViewModels{
        (requireActivity() as MainActivity).getMainViewModelFactory()
    }

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private var isTwoPane = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isTwoPane = arguments?.getBoolean("isTwoPane") == true
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = setupRecyclerView()
        setupUiStateObservers(adapter)
        setupMessageObservers()
        setupSortSpinner()
        setupLibraryButton()
        setupGoogleBooksButton()
        setupSwipeToDelete(adapter)
        setupFab()
        setupScrollSaveListener()
    }

    private fun setupRecyclerView(): LibraryAdapter {
        val adapter = LibraryAdapter(
            onItemClick = { item ->
                if (isTwoPane) {
                    viewModel.selectItem(item)
                } else {
                    val action = ListFragmentDirections.actionListFragmentToDetailFragment(item.id)
                    findNavController().navigate(action)
                }
            },
            onItemLongClick = { item ->
                Log.d("ListFragment", "Long click detected for item: ${item.id}")
                when (viewModel.selectedTab.value) {
                    Tab.LIBRARY -> viewModel.updateItemAvailability(item)
                    Tab.GOOGLE_BOOKS -> viewModel.saveGoogleBookToLibrary(item)
                    else -> null
                }
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
            setHasFixedSize(true)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    viewModel.loadItemsForScroll(firstVisibleItemPosition, lastVisibleItemPosition)
                }
            })
        }

        return adapter
    }

    private fun setupUiStateObservers(adapter: LibraryAdapter) = with(binding) {
        viewModel.initialLoadDone.observe(viewLifecycleOwner) {
            Log.d("ListFragment", "Initial data loaded")
        }

        viewModel.selectedTab.observe(viewLifecycleOwner) { tab ->
            when (tab) {
                MainViewModel.Tab.LIBRARY -> {
                    googleBooksFilterLayout.root.gone()
                    sortSpinner.show()
                }
                MainViewModel.Tab.GOOGLE_BOOKS -> {
                    val shouldShowFilters = !viewModel.items.value.isNullOrEmpty()
                    googleBooksFilterLayout.root.takeIf { shouldShowFilters }?.show()
                    sortSpinner.gone()
                }
            }
        }

        viewModel.items.observe(viewLifecycleOwner) { items ->
            Log.d("ListFragment", "Items received: ${items.size}")

            if (viewModel.isLoading.value == false) {
                if (items.isEmpty()) {
                    recyclerView.gone()
                    sortSpinner.gone()
                    emptyStateTextView.show()
                } else {
                    recyclerView.show()
                    sortSpinner.show()
                    emptyStateTextView.gone()
                }
            }

            adapter.submitList(items) {
                Log.d("ListFragment", "Adapter updated with ${items.size} items")
                viewModel.scrollPosition.value?.let { position ->
                    if (position in 0 until adapter.itemCount) {
                        recyclerView.smoothScrollToPosition(position)
                    }
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Log.d("ListFragment", "isLoading changed to: $isLoading")
            if (isLoading) {
                showLoadingState()
            } else {
                showContentState()
            }
        }

        viewModel.isScrollLoading.observe(viewLifecycleOwner) {
            if (it == true) loadMoreProgress.show() else loadMoreProgress.gone()
        }

        viewModel.scrollPosition.observe(viewLifecycleOwner) { position ->
            if (position in 0 until adapter.itemCount) {
                recyclerView.smoothScrollToPosition(position)
            }
        }
    }

    private fun setupMessageObservers() {
        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Error: ${it?.asString(requireContext())}", Toast.LENGTH_SHORT).show()
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it.asString(requireContext()), Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }
    }

    private fun setupSortSpinner() {
        val savedOrder = viewModel.getSavedSortOrder()
        val savedPosition = SortOrder.toSpinnerPosition(savedOrder)
        binding.sortSpinner.setSelection(savedPosition, false)

        binding.sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedOrder = SortOrder.fromSpinnerPosition(position)
                if (selectedOrder != viewModel.getSavedSortOrder()) {
                    viewModel.setSortOrder(selectedOrder)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupLibraryButton() = with(binding) {
        libraryButton.setOnClickListener {
            viewModel.selectTab(MainViewModel.Tab.LIBRARY)
        }

    }

    private fun setupGoogleBooksButton() = with(binding) {
        googleBooksButton.setOnClickListener {
            viewModel.selectTab(MainViewModel.Tab.GOOGLE_BOOKS)

            with(googleBooksFilterLayout) {
                val watcher = object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        searchButton.isEnabled =
                            titleEditText.text.length >= 3 || authorEditText.text.length >= 3
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                }

                titleEditText.addTextChangedListener(watcher)
                authorEditText.addTextChangedListener(watcher)

                searchButton.setOnClickListener {
                    val title = titleEditText.text.toString()
                    val author = authorEditText.text.toString()
                    if (title.length >= 3 || author.length >= 3) {
                        viewModel.searchBooksOnline(title, author)
                    }
                }
            }
        }
    }

    private fun setupSwipeToDelete(adapter: LibraryAdapter) {
        val itemTouchHelper = ItemTouchHelper(
            SwipeToDeleteCallback { position ->
                val currentItem = adapter.currentList[position]
                viewModel.removeItem(currentItem.id)
            }
        )
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            if (isTwoPane) {
                (activity as MainActivity).showAddForm()
            } else {
                findNavController().navigate(R.id.action_listFragment_to_addFragment)
            }
        }
    }

    private fun setupScrollSaveListener() {
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                viewModel.saveScrollPosition(layoutManager.findFirstVisibleItemPosition())
            }
        })
    }

    private fun showLoadingState() = with(binding) {
        shimmerLayout.startShimmer()
        shimmerLayout.show()
        sortSpinner.gone()
        recyclerView.gone()
        emptyStateTextView.gone()
        googleBooksFilterLayout.root.gone()
    }

    private fun showContentState() = with(binding) {
        shimmerLayout.stopShimmer()
        shimmerLayout.gone()
        if (viewModel.items.value?.isEmpty() == true) {
            recyclerView.gone()
            sortSpinner.gone()
            emptyStateTextView.show()
        } else {
            recyclerView.show()
            emptyStateTextView.gone()
            sortSpinner.takeIf { viewModel.selectedTab.value == MainViewModel.Tab.LIBRARY }?.show()
        }

        when (viewModel.selectedTab.value) {
            MainViewModel.Tab.GOOGLE_BOOKS -> {
                googleBooksFilterLayout.root.takeIf { viewModel.items.value.isNullOrEmpty() }?.show()
            }
            else -> googleBooksFilterLayout.root.gone()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
