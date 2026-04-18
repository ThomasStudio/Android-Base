package com.thomas.androidbase

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.thomas.androidbase.features.components.ComponentDemoScreen
import com.thomas.androidbase.features.components.ComponentsScreen
import com.thomas.androidbase.features.home.HomeScreen
import com.thomas.androidbase.features.news.NewsScreen
import com.thomas.androidbase.features.weibo.WeiboScreen
import com.thomas.androidbase.navigation.MainRoute
import com.thomas.base.navigation.asNavigator

/**
 * Created by thomas on 4/11/2026.
 */

@Composable
fun MainScreen(navController: NavHostController) {
    val navigator = navController.asNavigator()
    NavHost(navController = navController, startDestination = MainRoute.Home.path) {
        composable(MainRoute.Home.path) {
            HomeScreen(
                navigator = navigator
            )
        }
        composable(MainRoute.Weibo.path) {
            WeiboScreen(navigator = navigator)
        }
        composable(MainRoute.News.path) {
            NewsScreen(navigator = navigator)
        }
        composable(MainRoute.Components.path) {
            ComponentsScreen(navigator = navigator)
        }
        composable(
            route = MainRoute.ComponentDemo.ROUTE,
            arguments = listOf(
                navArgument("componentId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val componentId = backStackEntry.arguments?.getString("componentId") ?: ""
            ComponentDemoScreen(
                navigator = navigator,
                componentId = componentId
            )
        }
    }
}