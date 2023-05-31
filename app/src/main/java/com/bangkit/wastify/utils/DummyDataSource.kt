package com.bangkit.wastify.utils

import com.bangkit.wastify.R
import com.bangkit.wastify.data.model.Category

object DummyDataSource {
    fun getCategories(): List<Category> {
        return listOf(
            Category(
                id = "1",
                icon = R.drawable.ic_battery_alert_filled_24_white,
                image = R.drawable.dummy_waste_category,
                name = "Paper",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
            ),
            Category(
                id = "2",
                icon = R.drawable.ic_battery_alert_filled_24_white,
                image = R.drawable.dummy_waste_category,
                name = "Plastic",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
            ),
            Category(
                id = "3",
                icon = R.drawable.ic_battery_alert_filled_24_white,
                image = R.drawable.dummy_waste_category,
                name = "Metal",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
            ),
            Category(
                id = "4",
                icon = R.drawable.ic_battery_alert_filled_24_white,
                image = R.drawable.dummy_waste_category,
                name = "Battery",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
            ),
            Category(
                id = "5",
                icon = R.drawable.ic_battery_alert_filled_24_white,
                image = R.drawable.dummy_waste_category,
                name = "Biological",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
            ),
            Category(
                id = "6",
                icon = R.drawable.ic_battery_alert_filled_24_white,
                image = R.drawable.dummy_waste_category,
                name = "Glass",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
            ),
            Category(
                id = "7",
                icon = R.drawable.ic_battery_alert_filled_24_white,
                image = R.drawable.dummy_waste_category,
                name = "Cardboard",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
            ),
            Category(
                id = "8",
                icon = R.drawable.ic_battery_alert_filled_24_white,
                image = R.drawable.dummy_waste_category,
                name = "Clothes",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
            )
        )
    }
}