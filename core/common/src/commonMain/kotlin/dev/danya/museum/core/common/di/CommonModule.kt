package dev.danya.museum.core.common.di

import dev.danya.museum.core.common.dispatchers.AppDispatchers
import dev.danya.museum.core.common.dispatchers.DefaultAppDispatchers
import org.koin.dsl.module

val commonModule = module {
    single<AppDispatchers> { DefaultAppDispatchers() }
}
