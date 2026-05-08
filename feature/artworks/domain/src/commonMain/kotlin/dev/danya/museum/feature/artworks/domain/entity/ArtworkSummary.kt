package dev.danya.museum.feature.artworks.domain.entity

data class ArtworkSummary(
    val id: Int,
    val title: String,
    val primaryImageUrl: String?,
    val artistName: String?,
    val objectDate: String?,
    val department: String,
)
