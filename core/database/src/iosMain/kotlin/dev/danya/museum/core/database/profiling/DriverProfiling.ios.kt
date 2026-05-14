package dev.danya.museum.core.database.profiling

import app.cash.sqldelight.db.SqlDriver

actual fun wrapForProfiling(driver: SqlDriver): SqlDriver = driver
