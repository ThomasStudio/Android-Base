package com.thomas.androidbase.network

import android.content.Context
import com.thomas.base.domain.ApiResponseInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier

/**
 * Created by thomas on 4/11/2026.
 */

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