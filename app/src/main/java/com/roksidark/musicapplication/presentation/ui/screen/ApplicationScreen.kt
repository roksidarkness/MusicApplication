package com.roksidark.musicapplication.presentation.ui.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.roksidark.feature.MainViewModel
import com.roksidark.feature.artistSearch.ArtistSearchScreen
import com.roksidark.feature.details.DetailsScreen
import com.roksidark.feature.navigation.NavigationTree


@Composable
fun ApplicationScreen(navController: NavHostController) {

    val viewModel = hiltViewModel<MainViewModel>()

    NavHost(navController = navController, startDestination = NavigationTree.List.name) {

        composable(NavigationTree.List.name) {
            ArtistSearchScreen(viewModel = viewModel, navController = navController)
        }
        composable("${NavigationTree.Details.name}/{selected_item}") { backStackEntry ->
            DetailsScreen(
                backStackEntry.arguments?.getString("selected_item").orEmpty(),
                viewModel = viewModel
            )
        }
    }
}
