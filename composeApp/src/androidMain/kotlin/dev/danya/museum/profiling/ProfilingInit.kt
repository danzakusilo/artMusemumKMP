package dev.danya.museum.profiling

import android.util.Log
import dev.danya.museum.core.database.profiling.DatabaseProfiler
import dev.danya.museum.core.network.profiling.NetworkProfiler
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@OptIn(ExperimentalAtomicApi::class)
fun initProfiling() {
    NetworkProfiler.enabled = true
    NetworkProfiler.onRecord = { r ->
        val tag = if (r.durationTag.isNotEmpty()) " ${r.durationTag}" else ""
        val msg = buildString {
            appendLine("┌ ${r.method} ${r.statusCode} /${r.shortUrl} · ${r.durationMs}ms$tag")
            if (r.trace.isNotEmpty()) appendLine("│ ↳ ${r.trace}")
            append("└")
        }
        Log.d("Museum.Net", msg)
    }

    DatabaseProfiler.enabled.store(true)
    DatabaseProfiler.onRecord.store { r ->
        val tag = if (r.durationTag.isNotEmpty()) " ${r.durationTag}" else ""
        val msg = buildString {
            appendLine("┌ ${r.operation} · ${r.durationMs}ms$tag")
            appendLine("│ ${r.sqlPreview}")
            if (r.trace.isNotEmpty()) appendLine("│ ↳ ${r.trace}")
            append("└")
        }
        Log.d("Museum.DB", msg)
    }
}