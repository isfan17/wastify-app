package com.bangkit.wastify.ui.screens.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bangkit.wastify.R
import com.bangkit.wastify.data.model.Article
import com.bangkit.wastify.databinding.FragmentArticleDetailBinding

class ArticleDetailFragment : Fragment() {

    private var _binding: FragmentArticleDetailBinding? = null
    private val binding get() = _binding!!

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

        val objArticle = navArgs.article
        bind(objArticle)

        // Handle back btn
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun bind(article: Article) {
        binding.ivWaste.setImageResource(article.image)
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