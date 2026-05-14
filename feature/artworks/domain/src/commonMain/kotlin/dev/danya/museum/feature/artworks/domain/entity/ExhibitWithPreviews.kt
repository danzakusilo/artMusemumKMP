package dev.danya.museum.feature.artworks.domain.entity

data class ExhibitWithPreviews(
    val exhibit: Exhibit,
    val previewArtworks: List<ArtworkSummary>,
)