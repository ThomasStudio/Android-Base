# Android Development Instructions

## Architecture

Always use MVVM (Model-View-ViewModel) architecture for structuring all features in this Android project. This is a hard rule.

### **Models**: Handle data operations, API calls, and database interactions.

- Use retrofit2 for API handling.
- Create interface contracts for repositories to abstract data sources.
- Implement repositories that interact with APIs and databases.
- Use ApiResponseInterceptor to transform API responses into Response in base module.
- Use hilt to create repository, service etc. and inject dependencies.
- All API responses should be wrapped in a Response class in the base module, which contains code, message, and data fields.
- The repository should return a Result wrapper that indicates success or error.
- Flow to handle API calls and data transformations in repositories.

  - Add service interfaces for API endpoints.
  - use ApiResponseInterceptor to standardize API responses.
  - Implement repositories that call these services and handle responses.
  - Use Result wrapper to represent success or error states in repository methods.
  - Implement ErrorHandler interface to convert API responses and exceptions into Result.Error. Such as ApiErrorHandler.
  - Use hilt modules to provide Retrofit instances and repositories.
  - consume repository methods in ViewModels and update UI state accordingly.
- Service example

```kotlin
interface WeiboService {
    @GET("weibohot")
    suspend fun getNews(): Response<WeiboHot>
}

open class Response<T>(
    @SerializedName("code") val code: Int,
    @SerializedName("msg") val message: String?,
    @SerializedName("data") val data: T?
) {
    open fun isSuccess(): Boolean = code == 200
}

data class WeiboHot(
    @SerializedName("code")
    val code: Int,

    @SerializedName("msg")
    val msg: String,

    @SerializedName("data")
    val data: List<HotItem>
)

data class HotItem(
    @SerializedName("hot")
    val hot: String,

    @SerializedName("index")
    val index: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("url")
    val url: String
)
```

- Repository: base class for all repositories, which contains the common logic for handling API calls and errors. Repositories can extend this base class to reuse the runCall method and error handling logic.:

```kotlin
abstract class Repository {
    protected open val errorHandler: ErrorHandler = ApiErrorHandler

    suspend fun <T> runCall(
        call: suspend () -> Response<T>
    ): Result<T> {
        return try {
            val response = call()

            if (response.isSuccess() && response.data != null) {
                Result.Success(response.data)
            } else {
                errorHandler.toError(response)
            }
        } catch (e: Exception) {
            errorHandler.toError(e)
        }
    }
}
```

- Repository extension example

```kotlin
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

- ApiResponseInterceptor: transform API responses into Response in base module.

```kotlin
open class ApiResponseInterceptor : Interceptor {
    private val gson = Gson()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        val originalBodyString = originalResponse.peekBody(Long.MAX_VALUE).string()

        val apiResponse = ApiResponse(
            code = originalResponse.code,
            message = originalResponse.message,
            data = parseData(originalBodyString)
        )

        val newBodyJson = gson.toJson(apiResponse)
        val newBody = newBodyJson.toResponseBody("application/json".toMediaType())

        return originalResponse.newBuilder().body(newBody).build()
    }

    private fun parseData(bodyString: String): Any? {
        return try {
            JsonParser.parseString(bodyString)
        } catch (e: Exception) {
            bodyString
        }
    }

}
```

- Result wrapper

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val code: Int, val message: String?) : Result<Nothing>()
}
```

- ErrorHandler interface

```kotlin
interface ErrorHandler {
    fun <T> toError(response:Response<T>):Result.Error

    fun toError(e:Exception): Result.Error
}
```

- ApiErrorHandler, subclass of ErrorHandler

```kotlin
object ApiErrorHandler : ErrorHandler {
    const val SYSTEM_ERROR = -1
    const val NETWORK_ERROR = -2
    const val NO_CONTENT_ERROR = 204

    const val BUSINESS_ERROR_STR = "Business Error"
    const val HTTP_ERROR_STR = "HTTP Error"
    const val SYSTEM_ERROR_STR = "System Error"
    const val NETWORK_ERROR_STR = "Network Error"
    const val NO_CONTENT_ERROR_STR = "No Content found"
    const val UNKNOWN_ERROR_STR = "Unknown error"
    const val ERROR_STR = "Error"

    fun getErrorMessage(code: Int, message: String? = null): String {
        val errorMsg = message ?: ""

        return when (code) {
            SYSTEM_ERROR -> "$SYSTEM_ERROR_STR: $errorMsg"
            NETWORK_ERROR -> "$NETWORK_ERROR_STR: $errorMsg"
            NO_CONTENT_ERROR -> "$NO_CONTENT_ERROR_STR: $errorMsg"
            else -> "$ERROR_STR $code: ${message ?: UNKNOWN_ERROR_STR}"
        }
    }

    override fun <T> toError(response: Response<T>): Result.Error {
        return if (response.isSuccess() && response.data == null) {
            Result.Error(NO_CONTENT_ERROR, getErrorMessage(NO_CONTENT_ERROR, response.message))
        } else {
            Result.Error(response.code, response.message ?: BUSINESS_ERROR_STR)
        }
    }

    override fun toError(e: Exception) = when (e) {
        is HttpException -> Result.Error(
            e.code(),
            "$HTTP_ERROR_STR: ${e.response()?.errorBody()?.string() ?: e.message()}"
        )

        is IOException -> Result.Error(NETWORK_ERROR, getErrorMessage(NETWORK_ERROR, e.message))
        else -> Result.Error(SYSTEM_ERROR, getErrorMessage(SYSTEM_ERROR, e.message))
    }
}
```

- hilt module for repository

```kotlin
@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindWeiboRepository(impl: WeiboRepositoryImpl): WeiboRepository
}

```

- hilt module for network

```kotlin
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
- BaseContract

```kotlin
interface BaseContract<STATE : UIStateIF> {
    val uiState: StateFlow<STATE>
    val event: SharedFlow<Event>

    fun viewCreated() {}
  
    fun back()
}

data class UIState<DATA>(
    override val status: Status = Status.LOADING,
    override val data: DATA? = null,
    override val error: Error? = null
) : UIStateIF


interface UIStateIF {
    val status: Status
    val data: Any?
    val error: Error?
}

enum class Status {
    LOADING, SUCCESS, ERROR
}


open class Error(val code: Int = -1, val message: String = "")

interface Event

data class NavigateEvent(
    val route: String,
    val popUpToRoute: String? = null,
    val inclusive: Boolean = false,
    val launchSingleTop: Boolean = false,
) : Event

object BackEvent : Event
data class MessageEvent(val message: String) : Event

```

- subcontract example

```kotlin
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

- BaseViewModel

```kotlin
abstract class BaseViewModel<STATE : UIStateIF> : ViewModel(), BaseContract<STATE> {

    protected open val scope: CoroutineScope
        get() = viewModelScope

    private val _uiState = MutableStateFlow(initialState())
    override val uiState: StateFlow<STATE> = _uiState

    private val _event = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    override val event: SharedFlow<Event> = _event.asSharedFlow()

    protected abstract fun initialState(): STATE

    override fun back() {
        send(BackEvent)
    }

    protected fun updateState(state: STATE) {
        updateState { state }
    }

    protected fun updateState(reducer: STATE.() -> STATE) {
        _uiState.update(reducer)
    }

    protected fun send(viewEvent: Event) {
        if (_event.tryEmit(viewEvent)) return

        scope.launch { _event.emit(viewEvent) }
    }


    protected fun sendMessage(message: String) {
        send(MessageEvent(message))
    }

    protected fun navigate(
        route: String,
        popUpToRoute: String? = null,
        inclusive: Boolean = false,
        launchSingleTop: Boolean = false
    ) {
        send(NavigateEvent(route, popUpToRoute, inclusive, launchSingleTop))
    }

    protected fun navigate(
        route: AppRoute,
        popUpToRoute: AppRoute? = null,
        inclusive: Boolean = false,
        launchSingleTop: Boolean = false
    ) {
        send(NavigateEvent(route.path, popUpToRoute?.path, inclusive, launchSingleTop))
    }
}
```

- A new ViewModel should extend BaseViewModel and implement the subcontract.
- subviewmodel example

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

```kotlin
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

```kotlin
sealed class MainRoute(override val path: String) : AppRoute() {
    object Home : MainRoute("home")
    object Weibo : MainRoute("weibo")
    object News : MainRoute("news")
}
```

```kotlin
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
