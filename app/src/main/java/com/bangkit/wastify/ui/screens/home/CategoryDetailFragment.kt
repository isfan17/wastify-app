package com.bangkit.wastify.ui.screens.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bangkit.wastify.data.model.Category
import com.bangkit.wastify.databinding.FragmentCategoryDetailBinding

class CategoryDetailFragment : Fragment() {

    private var _binding: FragmentCategoryDetailBinding? = null
    private val binding get() = _binding!!

    private val navArgs: CategoryDetailFragmentArgs by navArgs()
    lateinit var category: Category

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

        val objCategory = navArgs.category
        bind(objCategory)
    }

    private fun bind(category: Category) {
        binding.ivWaste.setImageResource(category.image)
        binding.tvCategoryName.text = category.name
        binding.tvCategoryDescription.text = category.description
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}