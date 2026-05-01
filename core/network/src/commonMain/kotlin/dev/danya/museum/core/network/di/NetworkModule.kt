package dev.danya.museum.core.network.di

import dev.danya.museum.core.network.createHttpClient
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }
}
