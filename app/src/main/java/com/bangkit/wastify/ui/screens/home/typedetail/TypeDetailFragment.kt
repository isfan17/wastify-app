package com.bangkit.wastify.ui.screens.home.typedetail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bangkit.wastify.R
import com.bangkit.wastify.data.db.entities.CategoryEntity
import com.bangkit.wastify.data.model.TypeAndCategories
import com.bangkit.wastify.databinding.FragmentTypeDetailBinding
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TypeDetailFragment : Fragment() {

    private var _binding: FragmentTypeDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TypeDetailViewModel by viewModels()
    private val navArgs: TypeDetailFragmentArgs by navArgs()

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTypeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve type data
        viewModel.getType(navArgs.typeId)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.type.collectLatest {
                    if (it != null) { bind(it) }
                }
            }
        }

        // Handle back btn
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun bind(typeDetails: TypeAndCategories) {
        Glide.with(this)
            .load(typeDetails.type.icon)
            .into(binding.ivTypeIcon)
        Glide.with(this)
            .load(typeDetails.type.image)
            .placeholder(R.drawable.waste_img_placeholder)
            .into(binding.ivTypeImage)
        binding.tvTypeName.text = typeDetails.type.name
        binding.tvTypeDescription.text = typeDetails.type.description

        typeDetails.categories.forEach {
            binding.cgCategories.addView(createCategoryChip(requireContext(), it))
        }
    }

    private fun createCategoryChip(context: Context, category: CategoryEntity): Chip {
        return Chip(context).apply {
            text = category.name
            setOnClickListener {
                val action = TypeDetailFragmentDirections.actionTypeDetailFragmentToCategoryDetailFragment(category.id)
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}