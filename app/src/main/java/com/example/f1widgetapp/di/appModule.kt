package com.example.f1widgetapp.di

import com.example.f1widgetapp.data.LocalDataSource
import com.example.f1widgetapp.data.api.Api
import com.example.f1widgetapp.data.api.ApiInterface
import com.example.f1widgetapp.data.api.F1InfoApi
import com.example.f1widgetapp.data.repository.Repository
import com.example.f1widgetapp.data.repository.RepositoryInterface
import com.example.f1widgetapp.data.room.AppDatabase
import com.example.f1widgetapp.viewmodels.DriversViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    // Single instances
    single { AppDatabase.getDatabase(get()) }
    single { get<AppDatabase>().driverDao() }

    // API
    single<F1InfoApi> {
        Retrofit.Builder()
            .baseUrl("https://api.jolpi.ca/ergast/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(F1InfoApi::class.java)
    }

    single { LocalDataSource(get()) }

    single<ApiInterface> { Api() }

    // Repository
    single<RepositoryInterface> { Repository(driverDao = get(), remoteDataSource = get(), context = get()) }

    // ViewModels
    viewModel { DriversViewModel(repository = get()) }
}