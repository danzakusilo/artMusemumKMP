package dev.danya.museum.feature.artworks.ui.di

import dev.danya.museum.feature.artworks.domain.usecase.AddArtworkToExhibitUseCase
import dev.danya.museum.feature.artworks.domain.usecase.CreateExhibitUseCase
import dev.danya.museum.feature.artworks.domain.usecase.GetArtworkDetailUseCase
import dev.danya.museum.feature.artworks.domain.usecase.GetArtworkFeedUseCase
import dev.danya.museum.feature.artworks.domain.usecase.GetExhibitsUseCase
import dev.danya.museum.feature.artworks.domain.usecase.GetFavoritesUseCase
import dev.danya.museum.feature.artworks.domain.usecase.ToggleFavoriteUseCase
import dev.danya.museum.feature.artworks.ui.detail.ArtworkDetailViewModel
import dev.danya.museum.feature.artworks.ui.favorites.FavoritesViewModel
import dev.danya.museum.feature.artworks.ui.feed.SwipeFeedViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val artworksPresentationModule = module {
    factory { AddArtworkToExhibitUseCase(get()) }
    factory { CreateExhibitUseCase(get()) }
    factory { GetArtworkDetailUseCase(get()) }
    factory { GetArtworkFeedUseCase(get()) }
    factory { GetExhibitsUseCase(get()) }
    factory { GetFavoritesUseCase(get()) }
    factory { ToggleFavoriteUseCase(get()) }
    viewModel { params -> ArtworkDetailViewModel(params.get(), get(), get()) }
    viewModel { FavoritesViewModel(get(), get(), get(), get(), get()) }
    viewModel { SwipeFeedViewModel(get(), get()) }
}
