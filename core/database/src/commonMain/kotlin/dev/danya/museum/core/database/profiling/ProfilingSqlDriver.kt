package dev.danya.museum.core.database.profiling

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlPreparedStatement
import kotlin.time.TimeSource

class ProfilingSqlDriver(private val delegate: SqlDriver) : SqlDriver by delegate {

    override fun execute(
        identifier: Int?,
        sql: String,
        parameters: Int,
        binders: (SqlPreparedStatement.() -> Unit)?,
    ): QueryResult<Long> {
        if (!DatabaseProfiler.enabled) return delegate.execute(identifier, sql, parameters, binders)
        val start = TimeSource.Monotonic.markNow()
        val result = delegate.execute(identifier, sql, parameters, binders)
        val duration = start.elapsedNow().inWholeMilliseconds
        DatabaseProfiler.record(
            DbCallRecord(
                sql = sql,
                durationMs = duration,
                trace = TraceThreadLocal.get().orEmpty(),
            )
        )
        return result
    }

    override fun <R> executeQuery(
        identifier: Int?,
        sql: String,
        mapper: (SqlCursor) -> QueryResult<R>,
        parameters: Int,
        binders: (SqlPreparedStatement.() -> Unit)?,
    ): QueryResult<R> {
        if (!DatabaseProfiler.enabled) return delegate.executeQuery(identifier, sql, mapper, parameters, binders)
        val start = TimeSource.Monotonic.markNow()
        val result = delegate.executeQuery(identifier, sql, mapper, parameters, binders)
        val duration = start.elapsedNow().inWholeMilliseconds
        DatabaseProfiler.record(
            DbCallRecord(
                sql = sql,
                durationMs = duration,
                trace = TraceThreadLocal.get().orEmpty(),
            )
        )
        return result
    }
}