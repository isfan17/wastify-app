package com.bangkit.wastify.ui.screens.home.articledetail

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
import com.bangkit.wastify.utils.Helper.toast
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleDetailFragment : Fragment() {

    private var _binding: FragmentArticleDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArticleDetailViewModel by viewModels()
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
        viewModel.getArticle(navArgs.articleId)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.article.collectLatest {
                    if (it != null) { bind(it) }
                }
            }
        }

        // Handle back btn
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun bind(article: Article) {
        Glide.with(this)
            .load(article.image)
            .placeholder(R.drawable.waste_placeholder)
            .into(binding.ivArticle)
        binding.tvArticleTitle.text = article.title
        binding.tvArticleSource.text = article.source
        binding.tvArticlePublishedAt.text = article.publishedAt
        binding.tvArticleDescription.text = article.description
        binding.btnSave.isChecked = article.isBookmarked

        binding.btnSave.setOnClickListener {
            if (binding.btnSave.isChecked) {
                viewModel.deleteArticle(article)
                toast(getString(R.string.msg_article_saved))
            } else {
                viewModel.saveArticle(article)
                toast(getString(R.string.msg_article_deleted))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}