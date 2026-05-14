package dev.danya.museum.core.database.profiling

data class DbCallRecord(
    val sql: String,
    val durationMs: Long,
    val trace: String,
) {
    val operation: String
        get() = sql.trimStart().substringBefore(" ").uppercase().take(8)

    val sqlPreview: String
        get() = sql.replace(Regex("\\s+"), " ").trim()
            .let { if (it.length > 60) it.take(57) + "..." else it }

    val durationTag: String
        get() = when {
            durationMs >= 100 -> "⚠ SLOW"
            durationMs >= 30 -> "○"
            else -> ""
        }
}

object DatabaseProfiler {
    private val lock = Any()
    private val _records = mutableListOf<DbCallRecord>()
    val records: List<DbCallRecord> get() = synchronized(lock) { _records.toList() }

    @Volatile
    var enabled: Boolean = false

    @Volatile
    var slowThresholdMs: Long = 30

    @Volatile
    var onRecord: ((DbCallRecord) -> Unit)? = null

    fun record(entry: DbCallRecord) {
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
            println("[DB] No database calls recorded.")
            return
        }
        val total = snapshot.sumOf { it.durationMs }
        val avg = total / snapshot.size
        val max = snapshot.maxOf { it.durationMs }
        val slow = snapshot.count { it.durationMs >= slowThresholdMs }

        println(buildString {
            val w = 62
            val bar = "─".repeat(w)
            appendLine("[DB] ┌$bar┐")
            appendLine("[DB] │ DATABASE  ${snapshot.size} queries  ${total}ms total  ${avg}ms avg  ${if (slow > 0) "$slow slow" else "all OK"}".padEnd(w + 5) + "│")
            appendLine("[DB] ├$bar┤")
            snapshot.forEachIndexed { i, r ->
                val idx = "#${i + 1}".padEnd(4)
                val op = r.operation.padEnd(8)
                val dur = "${r.durationMs}ms".padStart(6)
                val tag = if (r.durationTag.isNotEmpty()) " ${r.durationTag}" else ""
                appendLine("[DB] │ $idx $op ${r.sqlPreview.padEnd(34)} $dur$tag".padEnd(w + 5) + "│")
                if (r.trace.isNotEmpty()) {
                    appendLine("[DB] │      ↳ ${r.trace}".padEnd(w + 5) + "│")
                }
            }
            appendLine("[DB] ├$bar┤")
            appendLine("[DB] │ Total: ${total}ms │ Avg: ${avg}ms │ Max: ${max}ms │ Slow(>${slowThresholdMs}ms): $slow".padEnd(w + 5) + "│")
            append("[DB] └$bar┘")
        })
    }
}