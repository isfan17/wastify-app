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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.wastify.R
import com.bangkit.wastify.data.model.User
import com.bangkit.wastify.databinding.FragmentHomeBinding
import com.bangkit.wastify.ui.adapters.ArticleCardAdapter
import com.bangkit.wastify.ui.adapters.CategoryGridAdapter
import com.bangkit.wastify.ui.adapters.TypeAdapter
import com.bangkit.wastify.utils.CustomGridSpacing
import com.bangkit.wastify.ui.viewmodels.AuthViewModel
import com.bangkit.wastify.ui.viewmodels.MainViewModel
import com.bangkit.wastify.utils.Helper.countFoundCategories
import com.bangkit.wastify.utils.Helper.toast
import com.bangkit.wastify.utils.UiState
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    private val typesAdapter by lazy {
        TypeAdapter(
            onItemClicked = {
                val action = HomeFragmentDirections.actionHomeFragmentToTypeDetailFragment(it.id)
                findNavController().navigate(action)
            }
        )
    }

    private val categoriesAdapter by lazy {
        CategoryGridAdapter(
            onItemClicked = {
                val action = HomeFragmentDirections.actionHomeFragmentToCategoryDetailFragment(it.id)
                findNavController().navigate(action)
            }
        )
    }

    private val articlesAdapter by lazy {
        ArticleCardAdapter(
            onItemClicked = {
                val action = HomeFragmentDirections.actionHomeFragmentToArticleDetailFragment(it.id)
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

        binding.btnInfo.setOnClickListener {
            context?.let { ctx ->
                MaterialAlertDialogBuilder(ctx)
                    .setTitle(getString(R.string.user_stats))
                    .setMessage(getString(R.string.msg_stats_rules))
                    .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

        // Recyclerview setup
        setupTypesRecyclerView()
        setupCategoriesRecyclerView()
        setupArticlesRecyclerView()

        // Get Data
        mainViewModel.getTypes()
        mainViewModel.getCategories()
        mainViewModel.getResults()

        // Retrieve data
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // User
                launch {
                    authViewModel.userFlow.collectLatest {
                        setUserData(it)
                    }
                }

                // Total Results
                launch {
                    mainViewModel.resultsFlow.collect {
                        if (it != null) {
                            when (it) {
                                UiState.Loading -> showStatsLoading(true)
                                is UiState.Failure -> {
                                    showStatsLoading(false)
                                    toast(it.error.toString())
                                }
                                is UiState.Success -> {
                                    showStatsLoading(false)
                                    binding.tvWasteIdentified.text = it.data.size.toString()
                                    binding.tvCategoriesFound.text = getString(R.string.value_categories_found, countFoundCategories(it.data))
                                }
                            }
                        }
                    }
                }

                // Types
                launch {
                    mainViewModel.typesFlow.collectLatest {
                        if (it != null) {
                            if (it.isNotEmpty()) typesAdapter.submitList(it)
                            else mainViewModel.fetchTypes()
                        }
                    }
                }
                launch {
                    mainViewModel.fetchTypesFlow.collectLatest {
                        if (it != null) {
                            when (it) {
                                UiState.Loading -> showTypesLoading(true)
                                is UiState.Failure -> {
                                    showTypesLoading(false)
                                    toast(it.error.toString())
                                }
                                is UiState.Success -> {
                                    showTypesLoading(false)
                                }
                            }
                        }
                    }
                }

                // Categories
                launch {
                    mainViewModel.categoriesFlow.collectLatest {
                        if (it != null) {
                            if (it.isNotEmpty()) categoriesAdapter.submitList(it)
                            else mainViewModel.fetchCategories()
                        }
                    }
                }
                launch {
                    mainViewModel.fetchCategoriesFlow.collectLatest {
                        if (it != null) {
                            when (it) {
                                UiState.Loading -> showCategoriesLoading(true)
                                is UiState.Failure -> {
                                    showCategoriesLoading(false)
                                    toast(it.error.toString())
                                }
                                is UiState.Success -> showCategoriesLoading(false)
                            }
                        }
                    }
                }

                // Articles
                launch {
                    mainViewModel.articlesFlow.collectLatest {
                        if (it != null) { articlesAdapter.submitList(it) }
                    }
                }
            }
        }

        // Move to history page
        binding.btnHistory.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_historyFragment)
        }

        // Move to settings page
        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }
    }

    private fun setupTypesRecyclerView() {
        val itemDecoration = CustomGridSpacing(requireContext(), R.dimen.type_item_offset)
        binding.rvTypes.layoutManager = GridLayoutManager(requireContext(), 5)
        binding.rvTypes.addItemDecoration(itemDecoration)
        binding.rvTypes.adapter = typesAdapter
    }

    private fun setupCategoriesRecyclerView() {
        val itemDecoration = CustomGridSpacing(requireContext(), R.dimen.category_item_offset)
        binding.rvCategories.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvCategories.addItemDecoration(itemDecoration)
        binding.rvCategories.adapter = categoriesAdapter
    }

    private fun setupArticlesRecyclerView() {
        binding.rvArticles.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvArticles.adapter = articlesAdapter
    }

    private fun setUserData(user: User?) {
        binding.tvGreetingsName.text = getString(R.string.value_full_name, user?.name)
        if (user?.imageUrl == null) {
            binding.ivProfile.setImageResource(R.drawable.default_profile)
        } else {
            Glide.with(this)
                .load(user.imageUrl)
                .into(binding.ivProfile)
        }
    }

    private fun showTypesLoading(state: Boolean) {
        binding.progressBarType.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun showCategoriesLoading(state: Boolean) {
        binding.progressBarCategory.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun showStatsLoading(state: Boolean) {
        binding.progressBarStatOne.visibility = if (state) View.VISIBLE else View.GONE
        binding.progressBarStatTwo.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun showArticlesLoading(state: Boolean) {
        binding.progressBarArticles.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}