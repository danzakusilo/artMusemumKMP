package dev.danya.museum

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform