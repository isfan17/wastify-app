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
import com.bangkit.wastify.data.model.Type
import com.bangkit.wastify.databinding.FragmentTypeDetailBinding
import com.bangkit.wastify.ui.adapters.TextAdapter
import com.bangkit.wastify.ui.viewmodels.MainViewModel
import com.bangkit.wastify.utils.Helper
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TypeDetailFragment : Fragment() {

    private var _binding: FragmentTypeDetailBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
    private val navArgs: TypeDetailFragmentArgs by navArgs()

    private lateinit var categoriesAdapter: TextAdapter

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
        mainViewModel.getTypeById(navArgs.typeId)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.typeFlow.collectLatest {
                    if (it != null) { bind(it) }
                }
            }
        }

        // Handle back btn
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun bind(type: Type) {
        Glide.with(this)
            .load(type.image)
            .placeholder(R.drawable.waste_placeholder)
            .into(binding.ivType)
        binding.tvTypeName.text = type.name
        binding.tvTypeDescription.text = type.description

        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        categoriesAdapter = TextAdapter(type.categories)
        binding.rvCategories.adapter = categoriesAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}