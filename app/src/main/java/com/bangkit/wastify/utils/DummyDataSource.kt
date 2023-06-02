package com.bangkit.wastify.utils

import com.bangkit.wastify.R
import com.bangkit.wastify.data.model.Article
import com.bangkit.wastify.data.model.Category

object DummyDataSource {
    fun getCategories(): List<Category> {
        return listOf(
            Category(
                id = "1",
                icon = R.drawable.ic_battery_alert_filled_24_white,
                image = R.drawable.dummy_waste_image,
                name = "Paper",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
            ),
            Category(
                id = "2",
                icon = R.drawable.ic_battery_alert_filled_24_white,
                image = R.drawable.dummy_waste_image,
                name = "Plastic",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
            ),
            Category(
                id = "3",
                icon = R.drawable.ic_battery_alert_filled_24_white,
                image = R.drawable.dummy_waste_image,
                name = "Metal",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
            ),
            Category(
                id = "4",
                icon = R.drawable.ic_battery_alert_filled_24_white,
                image = R.drawable.dummy_waste_image,
                name = "Battery",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
            ),
            Category(
                id = "5",
                icon = R.drawable.ic_battery_alert_filled_24_white,
                image = R.drawable.dummy_waste_image,
                name = "Biological",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
            ),
            Category(
                id = "6",
                icon = R.drawable.ic_battery_alert_filled_24_white,
                image = R.drawable.dummy_waste_image,
                name = "Glass",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
            ),
            Category(
                id = "7",
                icon = R.drawable.ic_battery_alert_filled_24_white,
                image = R.drawable.dummy_waste_image,
                name = "Cardboard",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
            ),
            Category(
                id = "8",
                icon = R.drawable.ic_battery_alert_filled_24_white,
                image = R.drawable.dummy_waste_image,
                name = "Clothes",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
            )
        )
    }

    fun getArticles(): List<Article> {
        return listOf(
            Article(
                id = "1",
                image = R.drawable.dummy_waste_image,
                title = "First News. Recycling centre to stay open despite closure plan.",
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