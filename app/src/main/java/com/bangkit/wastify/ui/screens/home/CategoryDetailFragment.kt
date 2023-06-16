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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.wastify.R
import com.bangkit.wastify.data.model.Category
import com.bangkit.wastify.databinding.FragmentCategoryDetailBinding
import com.bangkit.wastify.ui.adapters.TextAdapter
import com.bangkit.wastify.ui.viewmodels.MainViewModel
import com.bangkit.wastify.utils.Helper.formatListToString
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryDetailFragment : Fragment() {

    private var _binding: FragmentCategoryDetailBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
    private val navArgs: CategoryDetailFragmentArgs by navArgs()

    private lateinit var methodsAdapter: TextAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve category data
        mainViewModel.getCategoryById(navArgs.categoryId)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.categoryFlow.collectLatest {
                    if (it != null) { bind(it) }
                }
            }
        }

        // Handle back btn
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun bind(category: Category) {
        Glide.with(this)
            .load(category.image)
            .placeholder(R.drawable.waste_placeholder)
            .into(binding.ivCategory)
        binding.tvCategoryName.text = category.name
        binding.tvCategoryDescription.text = category.description

        binding.rvMethods.layoutManager = LinearLayoutManager(requireContext())
        methodsAdapter = TextAdapter(category.disposalMethods)
        binding.rvMethods.adapter = methodsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}