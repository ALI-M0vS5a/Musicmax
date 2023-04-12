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

package com.maximillianleonov.musicmax.core.datastore

import androidx.datastore.core.DataStore
import com.maximillianleonov.musicmax.core.model.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    val userData = userPreferences.data.map { preferences ->
        UserData(
            playingQueueIds = preferences.playingQueueIdsList,
            playingQueueIndex = preferences.playingQueueIndex,
            favoriteSongs = preferences.favoriteSongIdsMap.keys
        )
    }

    suspend fun setPlayingQueueIds(playingQueueIds: List<String>) {
        userPreferences.updateData {
            it.copy {
                this.playingQueueIds.run {
                    clear()
                    addAll(playingQueueIds)
                }
            }
        }
    }

    suspend fun setPlayingQueueIndex(playingQueueIndex: Int) {
        userPreferences.updateData {
            it.copy {
                this.playingQueueIndex = playingQueueIndex
            }
        }
    }

    suspend fun toggleFavoriteSong(id: String, isFavorite: Boolean) {
        userPreferences.updateData {
            it.copy {
                if (isFavorite) {
                    favoriteSongIds.put(id, true)
                } else {
                    favoriteSongIds.remove(id)
                }
            }
        }
    }
}