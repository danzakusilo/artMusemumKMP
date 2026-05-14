package dev.danya.museum.core.database.profiling

import dev.danya.museum.core.common.profiling.TraceContext
import kotlin.coroutines.coroutineContext

object TraceThreadLocal {
    private val threadLocal = ThreadLocal<String?>()

    fun get(): String? = threadLocal.get()
    fun set(value: String?) = threadLocal.set(value)
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
