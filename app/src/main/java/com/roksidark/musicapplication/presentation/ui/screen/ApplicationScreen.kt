package com.roksidark.musicapplication.presentation.ui.screen

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.roksidark.feature.artistSearch.ArtistSearchScreen
import com.roksidark.feature.details.DetailsScreen
import com.roksidark.feature.MainViewModel
import com.roksidark.musicapplication.presentation.navigation.NavigationTree


@Composable
fun ApplicationScreen() {

    val navController = rememberNavController()
    val viewModel = hiltViewModel<MainViewModel>()

    NavHost(navController = navController, startDestination = NavigationTree.List.name) {

        composable(NavigationTree.List.name) {
            ArtistSearchScreen(viewModel = viewModel, navController = navController)
        }
        composable("${NavigationTree.Details.name}/{selected_item}") { backStackEntry ->
            DetailsScreen(backStackEntry.arguments?.getString("selected_item").orEmpty(),
                viewModel = viewModel)
        }
    }
}

@Composable
@ReadOnlyComposable
fun textResource(@StringRes id: Int): CharSequence =
    LocalContext.current.resources.getText(id)