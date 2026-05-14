package dev.danya.museum.core.network.profiling

import io.ktor.client.*

expect fun HttpClientConfig<*>.installProfiling()
