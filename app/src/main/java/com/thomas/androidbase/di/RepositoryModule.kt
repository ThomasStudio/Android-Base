package com.thomas.androidbase.di

import com.thomas.androidbase.data.repositories.WeiboRepository
import com.thomas.androidbase.data.repositories.WeiboRepositoryImpl
import com.thomas.androidbase.data.repositories.ZhihuRepository
import com.thomas.androidbase.data.repositories.ZhihuRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by thomas on 4/11/2026.
 */

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindWeiboRepository(impl: WeiboRepositoryImpl): WeiboRepository

    @Binds
    @Singleton
    fun bindZhihuRepository(impl: ZhihuRepositoryImpl): ZhihuRepository
}