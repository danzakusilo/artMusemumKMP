package dev.danya.museum.core.database.profiling

import dev.danya.museum.core.common.profiling.TraceContext
import kotlin.coroutines.coroutineContext

expect object TraceThreadLocal {
    fun get(): String?
    fun set(value: String?)
}

suspend inline fun <T> withDbTrace(block: () -> T): T {
    val trace = coroutineContext[TraceContext]?.formatted()
    TraceThreadLocal.set(trace)
    try {
        return block()
    } finally {
        TraceThreadLocal.set(null)
    }
}