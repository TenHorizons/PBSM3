package com.example.pbsm3.model.service.module

import com.example.pbsm3.model.*
import com.example.pbsm3.model.service.*
import com.example.pbsm3.model.service.dataSource.*
import com.example.pbsm3.model.service.implementation.AccountServiceImpl
import com.example.pbsm3.model.service.LogServiceImpl
import com.example.pbsm3.model.service.implementation.StorageServiceImpl
import com.example.pbsm3.model.service.repository.*
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

    @Binds
    abstract fun provideStorageService(impl: StorageServiceImpl): StorageService

    @Binds
    abstract fun provideAccountDataSource(accDS:AccountDataSource):DataSource<Account>
    @Binds
    abstract fun provideCategoryDataSource(catDS:CategoryDataSource):DataSource<Category>
    @Binds
    abstract fun provideTransactionDataSource(transDS:TransactionDataSource):DataSource<Transaction>
    @Binds
    abstract fun provideBudgetItemDataSource(itemDS:BudgetItemDataSource):DataSource<BudgetItem>
    @Binds
    abstract fun provideAvailableDataSource(avaDS:AvailableDataSource):DataSource<Unassigned>

    @Binds
    abstract fun provideAccountRepository(accRepo:AccountRepository):Repository<Account>
    @Binds
    abstract fun provideCategoryRepository(catRepo:CategoryRepository):Repository<Category>
    @Binds
    abstract fun provideTransactionRepository(transRepo:TransactionRepository):Repository<Transaction>
    @Binds
    abstract fun provideBudgetItemRepository(itemRepo:BudgetItemRepository):Repository<BudgetItem>
    @Binds
    abstract fun provideAvailableRepository(avaRepo:UnassignedRepository):Repository<Unassigned>

    /*@Binds
    abstract fun provideConfigurationService(impl: ConfigurationServiceImpl): ConfigurationService*/
}