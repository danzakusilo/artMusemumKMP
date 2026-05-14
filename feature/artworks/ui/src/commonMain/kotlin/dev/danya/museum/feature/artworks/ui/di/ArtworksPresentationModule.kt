package dev.danya.museum.feature.artworks.ui.di

import dev.danya.museum.feature.artworks.domain.usecase.AddArtworkToExhibitUseCase
import dev.danya.museum.feature.artworks.domain.usecase.CreateExhibitUseCase
import dev.danya.museum.feature.artworks.domain.usecase.DeleteExhibitUseCase
import dev.danya.museum.feature.artworks.domain.usecase.GetArtworkDetailUseCase
import dev.danya.museum.feature.artworks.domain.usecase.GetArtworkFeedUseCase
import dev.danya.museum.feature.artworks.domain.usecase.GetExhibitArtworksUseCase
import dev.danya.museum.feature.artworks.domain.usecase.GetExhibitIdsForArtworkUseCase
import dev.danya.museum.feature.artworks.domain.usecase.GetExhibitsUseCase
import dev.danya.museum.feature.artworks.domain.usecase.GetExhibitsWithPreviewsUseCase
import dev.danya.museum.feature.artworks.domain.usecase.GetFavoritesUseCase
import dev.danya.museum.feature.artworks.domain.usecase.IsFavoriteUseCase
import dev.danya.museum.feature.artworks.domain.usecase.RenameExhibitUseCase
import dev.danya.museum.feature.artworks.domain.usecase.RemoveArtworkFromExhibitUseCase
import dev.danya.museum.feature.artworks.domain.usecase.ToggleFavoriteUseCase
import dev.danya.museum.feature.artworks.ui.detail.ArtworkDetailViewModel
import dev.danya.museum.feature.artworks.ui.exhibitions.ExhibitDetailViewModel
import dev.danya.museum.feature.artworks.ui.exhibitions.ExhibitionsViewModel
import dev.danya.museum.feature.artworks.ui.favorites.FavoritesViewModel
import dev.danya.museum.feature.artworks.ui.feed.SwipeFeedViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val artworksPresentationModule = module {
    factory { AddArtworkToExhibitUseCase(get()) }
    factory { CreateExhibitUseCase(get()) }
    factory { DeleteExhibitUseCase(get()) }
    factory { GetArtworkDetailUseCase(get()) }
    factory { GetArtworkFeedUseCase(get()) }
    factory { GetExhibitArtworksUseCase(get()) }
    factory { GetExhibitIdsForArtworkUseCase(get()) }
    factory { GetExhibitsUseCase(get()) }
    factory { GetExhibitsWithPreviewsUseCase(get()) }
    factory { GetFavoritesUseCase(get()) }
    factory { IsFavoriteUseCase(get()) }
    factory { RenameExhibitUseCase(get()) }
    factory { RemoveArtworkFromExhibitUseCase(get()) }
    factory { ToggleFavoriteUseCase(get()) }
    viewModel { params -> ArtworkDetailViewModel(params.get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { params -> ExhibitDetailViewModel(params.get(), get(), get()) }
    viewModel { ExhibitionsViewModel(get(), get(), get(), get()) }
    viewModel { FavoritesViewModel(get(), get(), get(), get(), get()) }
    viewModel { SwipeFeedViewModel(get(), get()) }
}