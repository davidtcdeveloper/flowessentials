package com.davidtiago.flowessentials.finalproject.progress.di

import com.davidtiago.flowessentials.finalproject.ComputationCache
import com.davidtiago.flowessentials.finalproject.MemoryComputationCache
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CacheDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ComputingDispatcher

@Module
@InstallIn(SingletonComponent::class)
abstract class ProgressModule {

    @Binds
    abstract fun bindsCache(memoryComputationCache: MemoryComputationCache): ComputationCache

    companion object {
        @Provides
        @CacheDispatcher
        fun providesCacheDispatcher(): CoroutineDispatcher =
            Executors.newSingleThreadExecutor().asCoroutineDispatcher()

        @Provides
        @ComputingDispatcher
        fun providesComputationDispatcher(): CoroutineDispatcher =
            Dispatchers.Default
    }
}
