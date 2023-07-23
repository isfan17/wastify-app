package com.bangkit.wastify.ui.views.identify.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.wastify.R
import com.bangkit.wastify.data.model.CategoryAndMethods
import com.bangkit.wastify.data.model.Classification
import com.bangkit.wastify.data.model.Result
import com.bangkit.wastify.data.model.TypeAndCategories
import com.bangkit.wastify.databinding.FragmentResultBinding
import com.bangkit.wastify.ui.adapters.TextAdapter
import com.bangkit.wastify.ui.components.ClassificationsBottomSheet
import com.bangkit.wastify.utils.Helper.doubleToPercentageString
import com.bangkit.wastify.utils.Helper.toast
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ResultViewModel by viewModels()

    private val navArgs: ResultFragmentArgs by navArgs()
    private lateinit var result: Result
    private var isDetail = false

    private lateinit var methodsAdapter: TextAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        Timber.d("""
            Result Details :
            $result
        """.trimIndent())

        viewModel.getCategory(result.categoryId)
        viewModel.getType(result.typeId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Category Details
                launch {
                    viewModel.category.collectLatest {
                        if (it != null) bindCategory(it)
                    }
                }

                // Type Details
                launch {
                    viewModel.type.collectLatest {
                        if (it != null) bindType(it)
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnHome.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_homeFragment)
        }

        binding.btnSave.setOnClickListener {
            viewModel.saveResult(result)
            toast(getString(R.string.msg_result_saved))
            findNavController().navigate(R.id.action_resultFragment_to_homeFragment)
        }
        binding.btnDelete.setOnClickListener {
            viewModel.deleteResult(result)
            toast(getString(R.string.msg_remove_result_success))
            findNavController().navigateUp()
        }
    }

    private fun setupUi() {
        result = navArgs.result
        isDetail = navArgs.isDetail
        bindIdentification(result)

        binding.apply {
            btnHome.isVisible = !isDetail
            btnSave.isVisible = !isDetail
            btnDelete.isVisible = isDetail
        }
    }

    private fun bindCategory(categoryDetails: CategoryAndMethods) {
        Glide.with(this)
            .load(categoryDetails.category.icon)
            .into(binding.ivCategoryIcon)
        binding.tvCategory.text = categoryDetails.category.name
        binding.cardCategory.setOnClickListener {
            val action = ResultFragmentDirections.actionResultFragmentToCategoryDetailFragment(
                categoryDetails.category.id
            )
            findNavController().navigate(action)
        }

        binding.rvMethods.layoutManager = LinearLayoutManager(requireContext())
        methodsAdapter = TextAdapter(categoryDetails.methods.map { it.method })
        binding.rvMethods.adapter = methodsAdapter
    }

    private fun bindType(typeDetails: TypeAndCategories) {
        Glide.with(this)
            .load(typeDetails.type.icon)
            .into(binding.ivTypeIcon)
        binding.tvType.text = typeDetails.type.name
        binding.cardType.setOnClickListener {
            val action =
                ResultFragmentDirections.actionResultFragmentToTypeDetailFragment(typeDetails.type.id)
            findNavController().navigate(action)
        }
    }

    private fun bindIdentification(result: Result) {
        binding.apply {
            ivResult.setImageBitmap(result.image)
            tvDate.text = result.createdAt
            tvResult.text = result.classifications[0].name
            chipPercentage.text = doubleToPercentageString(result.classifications[0].percentage)
            chipPercentage.setOnClickListener { showBottomSheet(result.classifications) }
            tvRecyclable.text = if (result.recyclable) getString(R.string.yes) else getString(R.string.no)
            ivRecyclableIcon.setImageResource(
                if (result.recyclable) R.drawable.recyclable_yes else R.drawable.recyclable_no
            )
        }
    }

    private fun showBottomSheet(classifications: List<Classification>) {
        val classificationsBottomSheet = ClassificationsBottomSheet(classifications)
        classificationsBottomSheet.show(childFragmentManager, ClassificationsBottomSheet.TAG)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}