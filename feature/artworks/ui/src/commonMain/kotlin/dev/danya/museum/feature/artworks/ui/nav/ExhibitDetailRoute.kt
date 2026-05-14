package dev.danya.museum.feature.artworks.ui.nav

import kotlinx.serialization.Serializable

@Serializable
data class ExhibitDetailRoute(val exhibitId: Long, val exhibitName: String)