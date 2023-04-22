/*
 * Copyright 2023 Maximillian Leonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.maximillianleonov.musicmax.feature.library

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maximillianleonov.musicmax.core.domain.model.AlbumModel
import com.maximillianleonov.musicmax.core.domain.model.ArtistModel
import com.maximillianleonov.musicmax.core.domain.usecase.GetAlbumUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.GetArtistUseCase
import com.maximillianleonov.musicmax.core.domain.usecase.ToggleFavoriteSongUseCase
import com.maximillianleonov.musicmax.core.media.common.MediaConstants
import com.maximillianleonov.musicmax.core.media.service.MusicServiceConnection
import com.maximillianleonov.musicmax.core.ui.mapper.asAlbum
import com.maximillianleonov.musicmax.core.ui.mapper.asArtist
import com.maximillianleonov.musicmax.feature.library.model.LibraryType
import com.maximillianleonov.musicmax.feature.library.navigation.getLibraryId
import com.maximillianleonov.musicmax.feature.library.navigation.getLibraryType
import com.maximillianleonov.musicmax.feature.library.util.getSongs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LibraryViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
    getArtistUseCase: GetArtistUseCase,
    getAlbumUseCase: GetAlbumUseCase,
    savedStateHandle: SavedStateHandle,
    private val toggleFavoriteSongUseCase: ToggleFavoriteSongUseCase
) : ViewModel() {
    val uiState = getInitialUiState(
        getArtistUseCase = getArtistUseCase,
        getAlbumUseCase = getAlbumUseCase,
        savedStateHandle = savedStateHandle
    )

    fun play(startIndex: Int = MediaConstants.DEFAULT_INDEX) =
        musicServiceConnection.playSongs(songs = uiState.value.getSongs(), startIndex = startIndex)

    fun shuffle() = musicServiceConnection.shuffleSongs(songs = uiState.value.getSongs())

    fun onToggleFavorite(id: String, isFavorite: Boolean) =
        viewModelScope.launch { toggleFavoriteSongUseCase(id, isFavorite) }

    private fun getInitialUiState(
        getArtistUseCase: GetArtistUseCase,
        getAlbumUseCase: GetAlbumUseCase,
        savedStateHandle: SavedStateHandle
    ): StateFlow<LibraryUiState> {
        val libraryId = savedStateHandle.getLibraryId()
        return when (savedStateHandle.getLibraryType()) {
            LibraryType.Artist -> {
                getArtistUseCase(artistId = libraryId.toLong())
                    .map(ArtistModel::asArtist)
                    .map(LibraryUiState::ArtistType)
            }

            LibraryType.Album -> {
                getAlbumUseCase(albumId = libraryId.toLong())
                    .map(AlbumModel::asAlbum)
                    .map(LibraryUiState::AlbumType)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = LibraryUiState.Loading
        )
    }
}