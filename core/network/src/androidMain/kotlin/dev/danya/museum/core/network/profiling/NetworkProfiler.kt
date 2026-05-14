package dev.danya.museum.core.network.profiling

import dev.danya.museum.core.common.profiling.TraceContext
import io.ktor.client.plugins.api.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.currentCoroutineContext
import kotlin.time.TimeSource

data class NetworkCallRecord(
    val method: String,
    val url: String,
    val statusCode: Int,
    val durationMs: Long,
    val trace: String,
) {
    val shortUrl: String
        get() = url.substringAfter("//").substringAfter("/", "")
            .let { if (it.length > 60) it.take(57) + "..." else it }

    val durationTag: String
        get() = when {
            durationMs >= 2000 -> "⚠ SLOW"
            durationMs >= 500 -> "○"
            else -> ""
        }
}

object NetworkProfiler {
    private val lock = Any()
    private val _records = mutableListOf<NetworkCallRecord>()
    val records: List<NetworkCallRecord> get() = synchronized(lock) { _records.toList() }

    @Volatile
    var enabled: Boolean = false

    @Volatile
    var slowThresholdMs: Long = 500

    @Volatile
    var onRecord: ((NetworkCallRecord) -> Unit)? = null

    fun record(entry: NetworkCallRecord) {
        if (!enabled) return
        synchronized(lock) { _records.add(entry) }
        onRecord?.invoke(entry)
    }

    fun clear() {
        synchronized(lock) { _records.clear() }
    }

    fun printSummary() {
        val snapshot = records
        if (snapshot.isEmpty()) {
            println("[NET] No network calls recorded.")
            return
        }
        val total = snapshot.sumOf { it.durationMs }
        val avg = total / snapshot.size
        val max = snapshot.maxOf { it.durationMs }
        val slow = snapshot.count { it.durationMs >= slowThresholdMs }

        println(buildString {
            val w = 62
            val bar = "─".repeat(w)
            appendLine("[NET] ┌$bar┐")
            appendLine("[NET] │ NETWORK  ${snapshot.size} calls  ${total}ms total  ${avg}ms avg  ${if (slow > 0) "$slow slow" else "all OK"}".padEnd(w + 6) + "│")
            appendLine("[NET] ├$bar┤")
            snapshot.forEachIndexed { i, r ->
                val idx = "#${i + 1}".padEnd(4)
                val method = r.method.padEnd(4)
                val status = "${r.statusCode}"
                val dur = "${r.durationMs}ms".padStart(7)
                val tag = if (r.durationTag.isNotEmpty()) " ${r.durationTag}" else ""
                appendLine("[NET] │ $idx $method $status ${r.shortUrl.padEnd(34)} $dur$tag".padEnd(w + 6) + "│")
                if (r.trace.isNotEmpty()) {
                    appendLine("[NET] │      ↳ ${r.trace}".padEnd(w + 6) + "│")
                }
            }
            appendLine("[NET] ├$bar┤")
            appendLine("[NET] │ Total: ${total}ms │ Avg: ${avg}ms │ Max: ${max}ms │ Slow(>${slowThresholdMs}ms): $slow".padEnd(w + 6) + "│")
            append("[NET] └$bar┘")
        })
    }
}

private val StartTimeKey = AttributeKey<TimeSource.Monotonic.ValueTimeMark>("net_profiler_start")
private val TraceKey = AttributeKey<String>("net_profiler_trace")

val NetworkProfilingPlugin = createClientPlugin("NetworkProfiling") {

    on(SendingRequest) { request, _ ->
        val trace = currentCoroutineContext()[TraceContext]?.formatted().orEmpty()
        request.attributes.put(TraceKey, trace)
        request.attributes.put(StartTimeKey, TimeSource.Monotonic.markNow())
    }

    onResponse { response ->
        if (!NetworkProfiler.enabled) return@onResponse
        val attrs = response.call.request.attributes
        val start = attrs.getOrNull(StartTimeKey) ?: return@onResponse
        val record = NetworkCallRecord(
            method = response.call.request.method.value,
            url = response.call.request.url.toString(),
            statusCode = response.status.value,
            durationMs = start.elapsedNow().inWholeMilliseconds,
            trace = attrs.getOrNull(TraceKey).orEmpty(),
        )
        NetworkProfiler.record(record)
    }
}