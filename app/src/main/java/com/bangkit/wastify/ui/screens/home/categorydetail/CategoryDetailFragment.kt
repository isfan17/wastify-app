package com.bangkit.wastify.ui.screens.home.categorydetail

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
import com.bangkit.wastify.data.model.CategoryAndMethods
import com.bangkit.wastify.databinding.FragmentCategoryDetailBinding
import com.bangkit.wastify.ui.adapters.TextAdapter
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryDetailFragment : Fragment() {

    private var _binding: FragmentCategoryDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CategoryDetailViewModel by viewModels()
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
        viewModel.getCategory(navArgs.categoryId)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.category.collectLatest {
                    if (it != null) { bind(it) }
                }
            }
        }

        // Handle back btn
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun bind(categoryDetails: CategoryAndMethods) {
        Glide.with(this)
            .load(categoryDetails.category.icon)
            .into(binding.ivCategoryIcon)
        Glide.with(this)
            .load(categoryDetails.category.image)
            .placeholder(R.drawable.waste_img_placeholder)
            .into(binding.ivCategoryImage)
        binding.tvCategoryName.text = categoryDetails.category.name
        binding.tvCategoryDescription.text = categoryDetails.category.description

        binding.rvMethods.layoutManager = LinearLayoutManager(requireContext())
        methodsAdapter = TextAdapter(categoryDetails.methods.map { it.method })
        binding.rvMethods.adapter = methodsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}