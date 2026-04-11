package com.thomas.base.navigation

import androidx.navigation.NavHostController

/**
 * Created by thomas on 4/11/2026.
 */

interface Navigator {
    fun navigate(
        route: String,
        popUpToRoute: String? = null,
        inclusive: Boolean = false,
        launchSingleTop: Boolean = false
    )

    fun back()
}

fun Navigator.navigate(route: String) = navigate(route, null, false, false)

fun NavHostController.asNavigator(): Navigator = object : Navigator {
    override fun navigate(
        route: String,
        popUpToRoute: String?,
        inclusive: Boolean,
        launchSingleTop: Boolean
    ) {
        navigate(route) {
            popUpToRoute?.let { popUpRoute ->
                popUpTo(popUpRoute) { this.inclusive = inclusive }
            }
            this.launchSingleTop = launchSingleTop
        }
    }

    override fun back() {
        popBackStack()
    }
}

class DefaultNavigator : Navigator {
    override fun navigate(
        route: String,
        popUpToRoute: String?,
        inclusive: Boolean,
        launchSingleTop: Boolean
    ) {
    }

    override fun back() {}
}