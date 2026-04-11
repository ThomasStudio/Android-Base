# Android Development Instructions

## Architecture

Always use MVVM (Model-View-ViewModel) architecture for structuring all features in this Android project. This is a hard rule.

### **Models**: Handle data operations, API calls, and database interactions.
- Use retrofit2 for API handling.
- Create interface contracts for repositories to abstract data sources.
- Implement repositories that interact with APIs and databases.
- Use ApiResponseInterceptor to transform API responses into Response in base module.

### **ViewModels**: Contain business logic, manage UI state, and communicate with Models.
- Create contracts for each ViewModel to define the expected state and events.
- All contracts should extend BaseContract in the base module.
- A new ViewModel should extend BaseViewModel and implement the new contract.
- All events should extend Event in base module.

### **Views**: Pure UI components that observe ViewModel state and trigger user actions.
- Use Jetpack Compose for building all UI components. Avoid XML layouts.
- In Compose, observe ViewModel uiState and event to update UI. Use BaseContract.collectUiState and BaseContract.handleEvents.

### **Navigation**: Use Jetpack Navigation Compose for handling navigation between screens.
- Use NavHost to set up navigation graph and define composable destinations.
- Use NavHostController for navigating between composables.
- Create sealed class Route to define all possible routes in AppRoute.
```kotlin Route example
sealed class MainRoute(override val path: String) : AppRoute() {
    object Home : MainRoute("home")
    object Weibo : MainRoute("weibo")
    object News : MainRoute("news")
}
```

```kotlin NavHost example
@Composable
fun MainScreen(navController: NavHostController) {
    val navigator = navController.asNavigator()
    NavHost(navController = navController, startDestination = MainRoute.Home.path) {
        composable(MainRoute.Home.path) {
            HomeScreen(
                onWeiboClick = { navController.navigate(MainRoute.Weibo.path) },
                onNewsClick = { navController.navigate(MainRoute.News.path) },
                navigator = navigator
            )
        }
        composable(MainRoute.Weibo.path) {
            WeiboScreen(navigator = navigator)
        }
        composable(MainRoute.News.path) {
            NewsScreen(navigator = navigator)
        }
    }
}
```

## Language

Use Kotlin exclusively for all new code development. This is a hard rule.

- Avoid introducing new Java code unless absolutely necessary for legacy compatibility.

## UI Framework

Use Jetpack Compose for building all user interfaces. This is a hard rule.

- Do not create new XML layouts; migrate existing ones to Compose when possible.

## Asynchronous Programming

Use Kotlin Coroutines for all asynchronous operations. This is a hard rule.

- Prefer `Flow` for reactive data streams and state management.
- Avoid traditional callbacks or RxJava unless interfacing with legacy code.

## Testing

Follow Test-Driven Development (TDD) practices. This is a hard rule.

- Write unit tests for ViewModels, repositories, and utility classes before implementing the code.
- Use JUnit and Mockito for unit testing.
- Write integration tests for data layers.
- Use Compose Test framework for UI component testing.

## Code Quality

- Follow official Kotlin coding conventions.
- Use meaningful, descriptive names for variables, functions, and classes.
- Keep functions small and focused on a single responsibility.
- Use Hilt dependency injection for managing dependencies.
- Implement proper error handling and logging.
