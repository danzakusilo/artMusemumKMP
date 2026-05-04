package dev.danya.museum.feature.artworks.domain.entity

data class Artwork(
    val id: Int,
    val title: String,
    val primaryImageUrl: String?,
    val artistName: String?,
    val objectDate: String?,
    val culture: String?,
    val period: String?,
    val dynasty: String?,
    val medium: String?,
    val dimensions: String?,
    val department: String,
    val classification: String?,
    val repository: String?,
)
