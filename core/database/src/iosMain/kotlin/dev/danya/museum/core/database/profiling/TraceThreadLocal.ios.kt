package dev.danya.museum.core.database.profiling

actual object TraceThreadLocal {
    private var value: String? = null

    actual fun get(): String? = value
    actual fun set(value: String?) { this.value = value }
}