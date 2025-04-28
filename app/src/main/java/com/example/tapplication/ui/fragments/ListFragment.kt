package com.example.tapplication.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
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
import com.example.tapplication.utils.gone
import com.example.tapplication.utils.show
import kotlinx.coroutines.launch

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

    private val viewModel: MainViewModel by activityViewModels()

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
                viewModel.updateItemAvailability(item)
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
            setHasFixedSize(true)
        }

        lifecycleScope.launch {
            viewModel.itemsFlow.collect { items ->
                Log.d("ListFragment", "Items received: ${items.size}")
                adapter.submitList(items) {
                    viewModel.scrollPosition.value?.let { position ->
                        binding.recyclerView.scrollToPosition(position)
                    }
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.shimmerLayout.startShimmer()
                binding.shimmerLayout.show()
                binding.recyclerView.gone()
            } else {
                binding.shimmerLayout.stopShimmer()
                binding.shimmerLayout.gone()
                binding.recyclerView.show()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Error: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }

        val itemTouchHelper = ItemTouchHelper(
            SwipeToDeleteCallback { position ->
                viewModel.removeItem(position)
            })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)



    viewModel.scrollPosition.observe(viewLifecycleOwner) { position ->
            binding.recyclerView.scrollToPosition(position)
        }

        binding.fab.setOnClickListener {
            if (isTwoPane) {
                (activity as MainActivity).showAddForm()
            } else {
                findNavController().navigate(
                    R.id.action_listFragment_to_addFragment
                )
            }
        }

        // Сохраняем позицию скролла при прокрутке
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                viewModel.saveScrollPosition(layoutManager.findFirstVisibleItemPosition())
            }
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
