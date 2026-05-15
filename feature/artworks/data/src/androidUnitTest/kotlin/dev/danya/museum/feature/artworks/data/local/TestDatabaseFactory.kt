package dev.danya.museum.feature.artworks.data.local

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.danya.museum.core.common.dispatchers.AppDispatchers
import dev.danya.museum.core.database.MuseumDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

fun createTestDatabase(): MuseumDatabase {
    val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    MuseumDatabase.Schema.create(driver)
    driver.execute(null, "PRAGMA foreign_keys = ON", 0)
    return MuseumDatabase(driver)
}

fun testDispatchers(): AppDispatchers {
    val dispatcher = UnconfinedTestDispatcher()
    return object : AppDispatchers {
        override val io: CoroutineDispatcher = dispatcher
        override val default: CoroutineDispatcher = dispatcher
        override val main: CoroutineDispatcher = dispatcher
    }
}
