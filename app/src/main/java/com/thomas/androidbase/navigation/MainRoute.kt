package com.thomas.androidbase.navigation

import com.thomas.base.navigation.AppRoute

sealed class MainRoute(override val path: String) : AppRoute() {
    object Home : MainRoute("home")
    object Weibo : MainRoute("weibo")
    object News : MainRoute("news")
}

