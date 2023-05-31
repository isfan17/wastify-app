package com.bangkit.wastify.ui.screens.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bangkit.wastify.R
import com.bangkit.wastify.databinding.FragmentHomeBinding
import com.bangkit.wastify.ui.adapters.CategoryGridAdapter
import com.bangkit.wastify.ui.adapters.CategoryGridSpacing
import com.bangkit.wastify.ui.viewmodels.AuthViewModel
import com.bangkit.wastify.ui.viewmodels.WasteViewModel
import com.bangkit.wastify.utils.Helper.toast
import com.bangkit.wastify.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()
    private val wasteViewModel: WasteViewModel by viewModels()

    private val categoriesAdapter by lazy {
        CategoryGridAdapter(
            onItemClicked = {
                val action = HomeFragmentDirections.actionHomeFragmentToCategoryDetailFragment(it)
                findNavController().navigate(action)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvGreetingsName.text = getString(R.string.value_full_name, authViewModel.currentUser?.displayName)

        // Category recyclerview setup
        setupCategoriesRecyclerView()

        // Retrieve categories data
        wasteViewModel.getCategories()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                wasteViewModel.categoriesFlow.collect { state ->
                    if (state != null) {
                        when (state) {
                            UiState.Loading -> showCategoryLoading(true)
                            is UiState.Failure -> {
                                showCategoryLoading(false)
                                toast(state.error.toString())
                            }
                            is UiState.Success -> {
                                showCategoryLoading(false)
                                categoriesAdapter.submitList(state.data)
                            }
                        }
                    }
                }
            }
        }

        // Move to settings page
        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }
    }

    private fun showCategoryLoading(state: Boolean) {
        binding.progressBarCategory.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun setupCategoriesRecyclerView() {
        val itemDecoration = CategoryGridSpacing(requireContext(), R.dimen.category_item_offset)
        binding.rvCategories.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvCategories.addItemDecoration(itemDecoration)
        binding.rvCategories.adapter = categoriesAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}