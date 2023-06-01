package com.bangkit.wastify.ui.screens.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bangkit.wastify.R
import com.bangkit.wastify.databinding.FragmentHistoryBinding
import com.bangkit.wastify.ui.adapters.HistoryFragmentsAdapter
import com.google.android.material.tabs.TabLayoutMediator

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: HistoryFragmentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = HistoryFragmentsAdapter(requireActivity())
        binding.viewPager.adapter = adapter

        // Attaching viewpager to tablayout
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = (binding.viewPager.adapter as HistoryFragmentsAdapter).fragmentNames[position]
        }.attach()

        // Handling back btn
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}