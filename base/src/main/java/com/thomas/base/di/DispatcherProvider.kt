package com.thomas.base.di

import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by thomas on 4/11/2026.
 */

interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}

class DefaultDispatcherProvider @Inject constructor() : DispatcherProvider {
    override val main: CoroutineDispatcher
        get() = kotlinx.coroutines.Dispatchers.Main
    override val io: CoroutineDispatcher
        get() = kotlinx.coroutines.Dispatchers.IO
    override val default: CoroutineDispatcher
        get() = kotlinx.coroutines.Dispatchers.Default
}