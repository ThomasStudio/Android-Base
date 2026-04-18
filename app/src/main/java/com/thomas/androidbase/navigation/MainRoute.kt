package com.thomas.androidbase.navigation

import com.thomas.base.navigation.AppRoute

sealed class MainRoute(override val path: String) : AppRoute() {
    object Home : MainRoute("home")
    object Weibo : MainRoute("weibo")
    object News : MainRoute("news")
    object Components : MainRoute("components")
    
    data class ComponentDemo(val componentId: String) : MainRoute("component-demo/$componentId") {
        companion object {
            const val ROUTE = "component-demo/{componentId}"
        }
    }
}