/*
 * Copyright 2022 Maximillian Leonov
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

package com.maximillianleonov.musicmax.navigation

import androidx.annotation.StringRes
import com.maximillianleonov.musicmax.core.designsystem.icon.Icon.ImageVectorIcon
import com.maximillianleonov.musicmax.core.designsystem.icon.MusicmaxIcons
import com.maximillianleonov.musicmax.feature.favorite.navigation.FavoriteRoute
import com.maximillianleonov.musicmax.feature.home.navigation.HomeRoute
import com.maximillianleonov.musicmax.feature.search.navigation.SearchRoute
import com.maximillianleonov.musicmax.feature.settings.navigation.SettingsRoute
import com.maximillianleonov.musicmax.feature.favorite.R as favoriteR
import com.maximillianleonov.musicmax.feature.home.R as homeR
import com.maximillianleonov.musicmax.feature.search.R as searchR
import com.maximillianleonov.musicmax.feature.settings.R as settingsR

enum class TopLevelDestination(
    val route: String,
    val icon: ImageVectorIcon,
    @StringRes val titleResource: Int
) {
    Home(
        route = HomeRoute,
        icon = MusicmaxIcons.Home,
        titleResource = homeR.string.home
    ),
    Search(
        route = SearchRoute,
        icon = MusicmaxIcons.Search,
        titleResource = searchR.string.search
    ),
    Favorite(
        route = FavoriteRoute,
        icon = MusicmaxIcons.Favorite,
        titleResource = favoriteR.string.favorite
    ),
    Settings(
        route = SettingsRoute,
        icon = MusicmaxIcons.Settings,
        titleResource = settingsR.string.settings
    )
}
