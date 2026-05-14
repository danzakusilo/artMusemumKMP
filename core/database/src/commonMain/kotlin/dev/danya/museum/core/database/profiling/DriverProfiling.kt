package dev.danya.museum.core.database.profiling

import app.cash.sqldelight.db.SqlDriver

expect fun wrapForProfiling(driver: SqlDriver): SqlDriver
