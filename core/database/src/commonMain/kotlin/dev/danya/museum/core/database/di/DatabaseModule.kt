package dev.danya.museum.core.database.di

import app.cash.sqldelight.db.SqlDriver
import dev.danya.museum.core.database.DatabaseDriverFactory
import dev.danya.museum.core.database.MuseumDatabase
import dev.danya.museum.core.database.profiling.ProfilingSqlDriver
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

expect fun platformDatabaseModule(): Module

val databaseModule = module {
    includes(platformDatabaseModule())
    single<SqlDriver>(named("raw")) { get<DatabaseDriverFactory>().create() }
    single<SqlDriver> { ProfilingSqlDriver(get(named("raw"))) }
    single { MuseumDatabase(get()) }
}
