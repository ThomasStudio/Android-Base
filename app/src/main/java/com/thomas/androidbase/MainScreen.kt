package com.thomas.androidbase

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.thomas.androidbase.features.components.ComponentDemoScreen
import com.thomas.androidbase.features.components.ComponentsScreen
import com.thomas.androidbase.features.home.HomeScreen
import com.thomas.androidbase.features.news.NewsScreen
import com.thomas.androidbase.features.weibo.WeiboScreen
import com.thomas.androidbase.navigation.MainRoute
import com.thomas.base.navigation.DefaultNavigator
import com.thomas.base.navigation.Navigator
import com.thomas.base.navigation.asNavigator

/**
 * Created by thomas on 4/11/2026.
 */

@Composable
fun MainScreen(navController: NavHostController) {
    val navigator = navController.asNavigator()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            NavigationBar(navigator)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            MainContent(navController = navController, navigator)
        }
    }
}

@Composable
fun NavigationBar(navigator: Navigator) {
    NavigationBar {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton({ navigator.back() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }

}

@Composable
fun MainContent(navController: NavHostController, navigator: Navigator) {
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
        composable(MainRoute.ComponentDemo.ROUTE) {
            ComponentDemoScreen(navigator = navigator)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NavigationBarPreview() {
    NavigationBar(DefaultNavigator())
}