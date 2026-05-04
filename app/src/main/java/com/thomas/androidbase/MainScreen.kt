package com.thomas.androidbase

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.thomas.androidbase.features.booking.BookingScreen
import com.thomas.androidbase.features.components.ComponentDemoScreen
import com.thomas.androidbase.features.components.ComponentsScreen
import com.thomas.androidbase.features.examples.ExampleScreen
import com.thomas.androidbase.features.home.HomeScreen
import com.thomas.androidbase.features.news.NewsScreen
import com.thomas.androidbase.features.webpage.WebScreen
import com.thomas.androidbase.features.weibo.WeiboScreen
import com.thomas.androidbase.navigation.MainRoute
import com.thomas.androidbase.ui.components.NavigationBar
import com.thomas.androidbase.ui.components.NavigationData
import com.thomas.base.navigation.Navigator
import com.thomas.base.navigation.asNavigator
import com.thomas.base.ui.HandleEvents
import com.thomas.base.ui.collectUIState

/**
 * Created by thomas on 4/11/2026.
 */

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: RootContract = hiltViewModel<RootViewModel>()
) {
    val uiState = viewModel.collectUIState()
    val data = uiState.data
    val navigator = navController.asNavigator()
    Store.rootVM = viewModel

    viewModel.HandleEvents(navigator)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            data?.let {
                NavigationBar(
                    it.childNavigation ?: NavigationData(
                        it.title,
                        viewModel::back,
                        navigator.canBack()
                    )
                )
            }
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
        composable(MainRoute.Booking.path) {
            BookingScreen(navigator = navigator)
        }
        composable(MainRoute.Components.path) {
            ComponentsScreen(navigator = navigator)
        }
        composable(MainRoute.ComponentDemo.withPayload()) {
            ComponentDemoScreen(navigator = navigator)
        }
        composable(MainRoute.Web.withPayload()) {
            WebScreen()
        }
        composable(MainRoute.Examples.path) {
            ExampleScreen(navigator = navigator)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NavigationBarPreview() {
    NavigationBar(NavigationData("Title", {}, true))
}