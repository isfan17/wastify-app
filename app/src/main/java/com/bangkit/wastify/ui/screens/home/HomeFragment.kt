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
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()
    private val wasteViewModel: MainViewModel by viewModels()

    private val typeAdapter by lazy {
        TypeAdapter(
            onItemClicked = {}
        )
    }

    private val categoriesAdapter by lazy {
        CategoryGridAdapter(
            onItemClicked = {
                val action = HomeFragmentDirections.actionHomeFragmentToCategoryDetailFragment(it)
                findNavController().navigate(action)
            }
        )
    }

    private val articlesAdapter by lazy {
        ArticleCardAdapter(
            onItemClicked = {
                val action = HomeFragmentDirections.actionHomeFragmentToArticleDetailFragment(it)
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

        // Recyclerview setup
        setupTypesRecyclerView()
        setupCategoriesRecyclerView()
        setupArticlesRecyclerView()

        // Retrieve data
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // User
                launch {
                    authViewModel.userFlow.collectLatest {
                        setUserData(it)
                    }
                }

                // Types
                launch {
                    wasteViewModel.typesFlow.collectLatest {
                        if (it != null) { typeAdapter.submitList(it) }
                    }
                }

                // Categories
                launch {
                    wasteViewModel.categoriesFlow.collectLatest {
                        if (it != null) { categoriesAdapter.submitList(it) }
                    }
                }

                // Articles
                launch {
                    wasteViewModel.articlesFlow.collectLatest {
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
        val itemDecoration = CustomGridSpacing(requireContext(), R.dimen.grid_item_offset)
        binding.rvTypes.layoutManager = GridLayoutManager(requireContext(), 5)
        binding.rvTypes.addItemDecoration(itemDecoration)
        binding.rvTypes.adapter = typeAdapter
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

    private fun showCategoryLoading(state: Boolean) {
        binding.progressBarCategory.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun showArticlesLoading(state: Boolean) {
        binding.progressBarArticles.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}