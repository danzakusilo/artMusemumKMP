package dev.danya.museum.feature.artworks.domain.entity

data class Exhibit(
    val id: Long,
    val name: String,
    val createdAt: Long,
    val artworkCount: Int,
)
