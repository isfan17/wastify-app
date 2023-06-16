package com.bangkit.wastify.utils

import com.bangkit.wastify.R
import com.bangkit.wastify.data.model.Article

object DummyDataSource {
    fun getArticles(): List<Article> {
        return listOf(
            Article(
                id = "1",
                image = R.drawable.dummy_waste_image,
                title = "What a Waste: An Updated Look into the Future of Solid Waste Management",
                source = "BBC News",
                publishedAt = "June 06, 2023",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
            ),
            Article(
                id = "2",
                image = R.drawable.dummy_waste_image,
                title = "Second News. Recycling centre to stay open despite closure plan.",
                source = "Google",
                publishedAt = "May 31, 2023",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
            ),
            Article(
                id = "3",
                image = R.drawable.dummy_waste_image,
                title = "Third News. Recycling centre to stay open despite closure plan.",
                source = "CNN",
                publishedAt = "April 21, 2023",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
            ),
            Article(
                id = "4",
                image = R.drawable.dummy_waste_image,
                title = "Forth News. Recycling centre to stay open despite closure plan.",
                source = "CNN",
                publishedAt = "January 31, 2023",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
            ),
            Article(
                id = "5",
                image = R.drawable.dummy_waste_image,
                title = "Fifth News. Recycling centre to stay open despite closure plan.",
                source = "BBC News",
                publishedAt = "August 31, 2023",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
            )
        )
    }
}