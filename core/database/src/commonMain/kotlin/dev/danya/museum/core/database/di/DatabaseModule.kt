package dev.danya.museum.core.database.di

import app.cash.sqldelight.db.SqlDriver
import dev.danya.museum.core.database.DatabaseDriverFactory
import dev.danya.museum.core.database.MuseumDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformDatabaseModule(): Module

val databaseModule = module {
    includes(platformDatabaseModule())
    single<SqlDriver> { get<DatabaseDriverFactory>().create() }
    single { MuseumDatabase(get()) }
}
