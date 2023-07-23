package com.bangkit.wastify.data.repositories.main

import com.bangkit.wastify.data.db.dao.ArticleDao
import com.bangkit.wastify.data.db.dao.CategoryDao
import com.bangkit.wastify.data.db.dao.DisposalMethodDao
import com.bangkit.wastify.data.db.dao.ResultDao
import com.bangkit.wastify.data.db.dao.TypeDao
import com.bangkit.wastify.data.db.entities.asDomainModel
import com.bangkit.wastify.data.model.Article
import com.bangkit.wastify.data.model.asEntityModel
import com.bangkit.wastify.data.network.FirebaseArticle
import com.bangkit.wastify.data.network.FirebaseCategory
import com.bangkit.wastify.data.network.FirebaseDisposalMethod
import com.bangkit.wastify.data.network.FirebaseResult
import com.bangkit.wastify.data.network.FirebaseType
import com.bangkit.wastify.data.network.asEntityModel
import com.bangkit.wastify.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val dbReference: DatabaseReference,
    private val typeDao: TypeDao,
    private val categoryDao: CategoryDao,
    private val disposalMethodDao: DisposalMethodDao,
    private val articleDao: ArticleDao,
    private val resultDao: ResultDao,
) : MainRepository {

    override fun getTypes() = typeDao.getTypes().map { it.asDomainModel() }
    override fun getCategories() = categoryDao.getCategories().map { it.asDomainModel() }
    override fun getArticles() = articleDao.getArticles().map { it.asDomainModel() }
    override fun getSavedArticles() = articleDao.getSavedArticles().map { it.asDomainModel() }
    override fun getSavedResults() = resultDao.getResults().map { it.asDomainModel() }


    override suspend fun getType(id: String) = typeDao.getTypeAndCategories(id)
    override suspend fun getCategory(id: String) = categoryDao.getCategoryAndMethods(id)
    override suspend fun getArticle(id: String) = articleDao.getArticle(id).map { it.asDomainModel() }

    override suspend fun fetchTypes(): UiState<String> {
        return try {
            // Fetch data from firebase realtime db
            val dataSnapshot = dbReference.child("types").get().await()
            val networkTypes = mutableListOf<FirebaseType>()
            for (snapshot in dataSnapshot.children) {
                val networkType = snapshot.getValue(FirebaseType::class.java)
                networkType?.let {
                    networkType.id = snapshot.key
                    networkTypes.add(it)
                }
            }

            // Insert fetched data into the local Room database
            typeDao.clearTypes()
            typeDao.insertTypes(networkTypes.asEntityModel())

            UiState.Success("Types fetched successfully")
        } catch (e: Exception) {
            Timber.e(e)
            UiState.Failure(e.message)
        }
    }

    override suspend fun fetchCategories(): UiState<String> {
        return try {
            // Fetch data from firebase realtime db
            val categoriesSnapshot = dbReference.child("category").get().await()
            val networkCategories = mutableListOf<FirebaseCategory>()
            for (snapshot in categoriesSnapshot.children) {
                val networkCategory = snapshot.getValue(FirebaseCategory::class.java)
                networkCategory?.let {
                    networkCategory.id = snapshot.key
                    networkCategories.add(it)
                }
            }

            // Insert fetched data into the local Room database
            categoryDao.clearCategories()
            categoryDao.insertCategories(networkCategories.asEntityModel())

            // Fetch disposal methods data from firebase realtime db
            val methodsSnapshot = dbReference.child("disposal_methods").get().await()
            val networkMethods = mutableListOf<FirebaseDisposalMethod>()
            for (snapshot in methodsSnapshot.children) {
                val networkMethod = snapshot.getValue(FirebaseDisposalMethod::class.java)
                networkMethod?.let {
                    networkMethod.id = snapshot.key
                    networkMethods.add(it)
                }
            }

            // Insert fetched disposal methods data into the local Room database
            disposalMethodDao.clearDisposalMethods()
            disposalMethodDao.insertDisposalMethods(networkMethods.asEntityModel())

            UiState.Success("Categories fetched successfully")
        } catch (e: Exception) {
            Timber.e(e)
            UiState.Failure(e.message)
        }
    }

    override suspend fun fetchArticles(): UiState<String> {
        return try {
            // Fetch articles data from firebase realtime db
            val articlesSnapshot = dbReference.child("articles").get().await()
            val networkArticles = mutableListOf<FirebaseArticle>()
            for (snapshot in articlesSnapshot.children) {
                val networkArticle = snapshot.getValue(FirebaseArticle::class.java)
                networkArticle?.let {
                    networkArticle.id = snapshot.key
                    networkArticles.add(it)
                }
            }

            // Insert fetched articles data into the local Room database
            articleDao.clearArticles()
            articleDao.insertArticles(networkArticles.asEntityModel())

            // Fetch saved articles data from firebase realtime db
            val savedArticlesSnapshot = firebaseAuth.currentUser?.let { dbReference.child("saved_articles").child(it.uid).get().await() }
            if (savedArticlesSnapshot != null) {
                for (snapshot in savedArticlesSnapshot.children) {
                    val savedArticleId = snapshot.key
                    val article = articleDao.checkArticle(savedArticleId!!)
                    article?.let {
                        // Update the isBookmarked attribute to true
                        it.isBookmarked = true
                        // Update the article in the database
                        articleDao.updateArticle(it)
                    }
                }
            }

            UiState.Success("Articles fetched successfully")
        } catch (e: Exception) {
            Timber.e(e)
            UiState.Failure(e.message)
        }
    }

    override suspend fun fetchResults(): UiState<String> {
        return try {
            val localResults = resultDao.getResults().take(1).single()
            if (localResults.isEmpty()) {
                // Fetch saved results data from firebase realtime db
                val resultsSnapshot =
                    firebaseAuth.currentUser?.let { dbReference.child("saved_results").child(it.uid).get().await() }
                val networkResults = mutableListOf<FirebaseResult>()
                if (resultsSnapshot != null) {
                    for (snapshot in resultsSnapshot.children) {
                        val networkResult = snapshot.getValue(FirebaseResult::class.java)
                        networkResult?.let {
                            networkResults.add(it)
                        }
                    }
                }

                // Insert fetched saved results into the local Room database
                resultDao.insertResults(networkResults.asEntityModel())
            }
            UiState.Success("Saved results fetched successfully")
        } catch (e: Exception) {
            Timber.e(e)
            UiState.Failure(e.message)
        }
    }

    override suspend fun setArticleBookmark(article: Article, bookmarkState: Boolean) {
        article.isBookmarked = bookmarkState
        articleDao.updateArticle(article.asEntityModel())
    }
}