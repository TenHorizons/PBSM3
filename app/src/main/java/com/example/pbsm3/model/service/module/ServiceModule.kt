package com.example.pbsm3.model.service.module

import com.example.pbsm3.model.service.AccountService
import com.example.pbsm3.model.service.LogService
import com.example.pbsm3.model.service.implementation.AccountServiceImpl
import com.example.pbsm3.model.service.implementation.LogServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds
    abstract fun provideLogService(impl: LogServiceImpl): LogService

    /*@Binds
    abstract fun provideStorageService(impl: StorageServiceImpl): StorageService

    @Binds
    abstract fun provideConfigurationService(impl: ConfigurationServiceImpl): ConfigurationService*/
}