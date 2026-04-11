# Android Development Instructions

## Architecture

Always use MVVM (Model-View-ViewModel) architecture for structuring all features in this Android project. This is a hard rule.

### **Models**: Handle data operations, API calls, and database interactions.
- Use retrofit2 for API handling.
- Create interface contracts for repositories to abstract data sources.
- Implement repositories that interact with APIs and databases.
- Use ApiResponseInterceptor to transform API responses into Response in base module.
```kotlin service example
interface WeiboService {
    @GET("weibohot")
    suspend fun getNews(): Response<WeiboHot>
}
```

```kotlin WeiboRepository.kt
interface WeiboRepository {
    suspend fun getWeiboHot(): Result<WeiboHot>
}

class WeiboRepositoryImpl @Inject constructor(
    private val weiboService: WeiboService
) : WeiboRepository, Repository() {
    override suspend fun getWeiboHot(): Result<WeiboHot> {
        return runCall { weiboService.getNews() }
    }
}
```

```kotlin repository di example
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindWeiboRepository(impl: WeiboRepositoryImpl): WeiboRepository
}

```

```kotlin di example
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ZhihuRetrofit


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeiboRetrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context
    ) = OkHttpClient.Builder()
        .addInterceptor(ApiResponseInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply { level = Level.BODY })
        .build()

    @ZhihuRetrofit
    @Provides
    fun provideZhihuRetrofit(okHttpClient: OkHttpClient): Retrofit =
        getRetrofit(BaseUrl.Zhihu.url, okHttpClient)

    @Provides
    fun provideZhihuService(@ZhihuRetrofit retrofit: Retrofit): ZhihuService =
        retrofit.create(ZhihuService::class.java)

    @WeiboRetrofit
    @Provides
    fun provideWeiboRetrofit(okHttpClient: OkHttpClient): Retrofit =
        getRetrofit(BaseUrl.Weibo.url, okHttpClient)

    @Provides
    fun provideWeiboService(@WeiboRetrofit retrofit: Retrofit): WeiboService =
        retrofit.create(WeiboService::class.java)

}

private fun getRetrofit(baseUrl: String, okHttpClient: OkHttpClient) = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
```


### **ViewModels**: Contain business logic, manage UI state, and communicate with Models.
- Create contracts for each ViewModel to define the expected state and events.
- All contracts should extend BaseContract in the base module.
```Kotlin Contract example
interface WeiboContract : BaseContract<UIState<WeiboHot>> {
    fun showMessage()
}

data class WeiboHot(
    val code: Int,
    val msg: String,
    val data: List<HotItem>
)

data class HotItem(
    val hot: String,
    val index: Int,
    val title: String,
    val url: String
)
```
- A new ViewModel should extend BaseViewModel and implement the new contract.
```kotlin
@HiltViewModel
class WeiboViewModel @Inject constructor(
    private val weiboRepository: WeiboRepository
) : BaseViewModel<UIState<WeiboHot>>(), WeiboContract {
    override fun initialState() = UIState<WeiboHot>()

    override fun showMessage() {
        send(MessageEvent("Hello from WeiboViewModel"))
    }

    private fun getWeiboHot() {
        scope.launch {
            when (val result = weiboRepository.getWeiboHot()) {
                is Result.Success -> {
                    updateState { copy(status = Status.SUCCESS, data = result.data) }
                }

                is Result.Error -> {
                    updateState {
                        copy(
                            status = Status.ERROR,
                            error = Error(code = result.code, message = result.message ?: "")
                        )
                    }
                }
            }
        }
    }

    override fun viewCreated() {
        getWeiboHot()
    }

}
```
- All events should extend Event in base module.


### **Views**: Pure UI components that observe ViewModel state and trigger user actions.
- Use Jetpack Compose for building all UI components. Avoid XML layouts.
- In Compose, observe ViewModel uiState and event to update UI. Use BaseContract.collectUiState and BaseContract.handleEvents.
```kotlin example
@Composable
fun WeiboScreen(
    navigator: Navigator,
    viewModel: WeiboContract = hiltViewModel<WeiboViewModel>()
) {
    val uiState = viewModel.collectUiState()
    val context = LocalContext.current

    viewModel.handleEvents(navigator = navigator) {
        when (it) {
            is MessageEvent -> {
                android.widget.Toast.makeText(
                    context,
                    it.message,
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Call getWeiboHot when the screen is first composed
    LaunchedEffect(Unit) {
        viewModel.viewCreated()
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Weibo Screen")
            Button(onClick = viewModel::showMessage) {
                Text("Show Message")
            }

            when (uiState.status) {
                com.thomas.base.viewmodel.Status.LOADING -> {
                    LoadingScreen()
                }

                com.thomas.base.viewmodel.Status.SUCCESS -> {
                    uiState.data?.let { weiboHot ->
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(weiboHot.data) { item ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "${item.index}. ${item.title}",
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(text = item.hot)
                                }
                                HorizontalDivider(
                                    Modifier,
                                    DividerDefaults.Thickness,
                                    DividerDefaults.color
                                )
                            }
                        }
                    }
                }

                com.thomas.base.viewmodel.Status.ERROR -> {
                    Text(text = "Error: ${uiState.error?.message}")
                }
            }
        }
    }
}
```
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
