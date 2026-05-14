package dev.danya.museum.core.database.profiling

actual object TraceThreadLocal {
    private val threadLocal = ThreadLocal<String?>()

    actual fun get(): String? = threadLocal.get()
    actual fun set(value: String?) = threadLocal.set(value)
}