package com.kyant.pixelmusic.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FeaturedPlayList
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material.icons.outlined.NewReleases
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.kyant.pixelmusic.ui.component.TopBar
import com.kyant.pixelmusic.ui.layer.BackLayer
import com.kyant.pixelmusic.ui.layer.ForeLayer
import com.kyant.pixelmusic.ui.layer.component1
import com.kyant.pixelmusic.ui.layer.component2
import com.kyant.pixelmusic.ui.layout.TwoColumnGrid
import com.kyant.pixelmusic.ui.shape.SuperellipseCornerShape
import com.kyant.pixelmusic.ui.theme.androidBlue
import com.kyant.pixelmusic.ui.theme.androidGreen
import com.kyant.pixelmusic.ui.theme.androidOrange
import com.kyant.pixelmusic.util.Quadruple
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun Home(navController: NavHostController) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = FocusRequester.Default
    val scope = rememberCoroutineScope()
    val (searchState, accountState) = rememberSwipeableState(false)
    BackHandler(accountState.targetValue or searchState.targetValue) {
        scope.launch {
            if (accountState.targetValue)
                accountState.animateTo(false, spring(stiffness = 700f))
            else if (searchState.targetValue)
                searchState.animateTo(false, spring(stiffness = 700f))
        }
    }
    BackLayer(searchState, accountState) {
        TopBar(searchState, accountState, Modifier.zIndex(1f))
        Column(Modifier.padding(top = 64.dp)) {
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                SuperellipseCornerShape(12.dp),
                MaterialTheme.colors.secondary,
                elevation = 24.dp
            ) {
                Column(
                    Modifier
                        .clickable {}
                        .padding(32.dp)
                ) {
                    Text(
                        "Pixel Music Alpha",
                        style = MaterialTheme.typography.h5
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Still developing...",
                        style = MaterialTheme.typography.body1
                    )
                }
            }
            Spacer(Modifier.height(32.dp))
            TwoColumnGrid(
                listOf(
                    Quadruple(
                        "Playlists",
                        Icons.Outlined.FeaturedPlayList, androidBlue,
                        Screens.MyPlaylists.name
                    ),
                    Quadruple(
                        "New releases",
                        Icons.Outlined.NewReleases, androidOrange,
                        Screens.NewReleases.name
                    ),
                    Quadruple(
                        "Leaderboards",
                        Icons.Outlined.Leaderboard, androidGreen,
                        Screens.Leaderboards.name
                    )
                ),
                { navController.navigate(it) }
            )
        }
    }
    ForeLayer(searchState) {
        Search(focusRequester)
        LaunchedEffect(searchState.targetValue) {
            if (searchState.targetValue) {
                focusRequester.requestFocus()
                keyboardController?.showSoftwareKeyboard()
            } else {
                focusRequester.freeFocus()
                keyboardController?.hideSoftwareKeyboard()
            }
        }
    }
    ForeLayer(accountState) {
        Account()
    }
}