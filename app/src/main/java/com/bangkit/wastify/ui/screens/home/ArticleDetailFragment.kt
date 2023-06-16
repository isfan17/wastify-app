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
import com.bangkit.wastify.R
import com.bangkit.wastify.data.model.Article
import com.bangkit.wastify.databinding.FragmentArticleDetailBinding
import com.bangkit.wastify.ui.viewmodels.MainViewModel
import com.bangkit.wastify.utils.Helper.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleDetailFragment : Fragment() {

    private var _binding: FragmentArticleDetailBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()
    private val navArgs: ArticleDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve article data
        mainViewModel.getArticleById(navArgs.articleId)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.articleFlow.collectLatest {
                    if (it != null) { bind(it) }
                }
            }
        }

        // Handle back btn
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSave.setOnClickListener {
            toast(getString(R.string.msg_feature_under_development))
        }
    }

    private fun bind(article: Article) {
        binding.ivArticle.setImageResource(article.image)
        binding.tvArticleTitle.text = article.title
        binding.tvArticleSource.text = article.source
        binding.tvArticlePublishedAt.text = article.publishedAt
        binding.tvArticleDescription.text = article.description
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}