package com.thomas.androidbase.network

import android.content.Context
import com.thomas.base.domain.ApiResponseInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Created by thomas on 4/11/2026.
 */

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ZhihuRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeiboRetrofit

/**
 * Network configuration constants
 */
private object NetworkConfig {
    const val CONNECT_TIMEOUT_SECONDS = 30L
    const val READ_TIMEOUT_SECONDS = 30L
    const val WRITE_TIMEOUT_SECONDS = 30L
    const val CACHE_SIZE_BYTES = 10L * 1024 * 1024 // 10MB
    const val CACHE_DIRECTORY_NAME = "http_cache"
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpCache(@ApplicationContext context: Context): Cache {
        val cacheDirectory = File(context.cacheDir, NetworkConfig.CACHE_DIRECTORY_NAME)
        return Cache(cacheDirectory, NetworkConfig.CACHE_SIZE_BYTES)
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        cache: Cache,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(ApiResponseInterceptor())
            .addInterceptor(loggingInterceptor)
            .connectTimeout(NetworkConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(NetworkConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(NetworkConfig.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    @ZhihuRetrofit
    @Provides
    @Singleton
    fun provideZhihuRetrofit(okHttpClient: OkHttpClient): Retrofit =
        createRetrofit(BaseUrl.Zhihu.url, okHttpClient)

    @Provides
    @Singleton
    fun provideZhihuService(@ZhihuRetrofit retrofit: Retrofit): ZhihuService =
        retrofit.create(ZhihuService::class.java)

    @WeiboRetrofit
    @Provides
    @Singleton
    fun provideWeiboRetrofit(okHttpClient: OkHttpClient): Retrofit =
        createRetrofit(BaseUrl.Weibo.url, okHttpClient)

    @Provides
    @Singleton
    fun provideWeiboService(@WeiboRetrofit retrofit: Retrofit): WeiboService =
        retrofit.create(WeiboService::class.java)
}

/**
 * Creates a Retrofit instance with common configuration
 */
private fun createRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}