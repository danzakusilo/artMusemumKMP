package dev.danya.museum.core.common.profiling

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.withContext

class TraceContext(
    val chain: List<String> = emptyList(),
) : AbstractCoroutineContextElement(Key) {
    companion object Key : CoroutineContext.Key<TraceContext>

    fun push(label: String) = TraceContext(chain + label)

    fun formatted(): String = chain.joinToString(" → ")
}

suspend fun <T> withTrace(label: String, block: suspend () -> T): T {
    val current = coroutineContext[TraceContext]
    val next = current?.push(label) ?: TraceContext(listOf(label))
    return withContext(next) { block() }
}