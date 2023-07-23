package com.bangkit.wastify.ui.views.bookmarks

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
import com.bangkit.wastify.databinding.FragmentSavedResultsBinding
import com.bangkit.wastify.ui.adapters.ResultGridAdapter
import com.bangkit.wastify.utils.CustomGridSpacing
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SavedResultsFragment : Fragment() {

    private var _binding: FragmentSavedResultsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookmarksViewModel by viewModels()

    private val resultsAdapter by lazy {
        ResultGridAdapter(
            onItemClicked = {
                val action = BookmarksFragmentDirections.actionBookmarksFragmentToResultFragment(
                    result = it,
                    isDetail = true
                )
                findNavController().navigate(action)
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupResultsRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.results.collectLatest {
                    if (it != null) {
                        showEmptyMsg(it.isEmpty())
                        resultsAdapter.submitList(it)
                    }
                }
            }
        }
    }

    private fun showEmptyMsg(state: Boolean) {
        binding.ivNoData.visibility = if (state) View.VISIBLE else View.GONE
        binding.tvTitleNoData.visibility = if (state) View.VISIBLE else View.GONE
        binding.tvDescNoData.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun setupResultsRecyclerView() {
        val itemDecoration = CustomGridSpacing(requireContext(), R.dimen.result_item_offset)
        binding.rvResults.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvResults.addItemDecoration(itemDecoration)
        binding.rvResults.adapter = resultsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}