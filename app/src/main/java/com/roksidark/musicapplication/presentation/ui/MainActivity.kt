package com.roksidark.musicapplication.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.roksidark.feature.navigation.NavigationTree
import com.roksidark.musicapplication.R
import com.roksidark.musicapplication.presentation.theme.AppTheme
import com.roksidark.musicapplication.presentation.theme.MusicApplicationTheme
import com.roksidark.musicapplication.presentation.ui.screen.ApplicationScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class,
)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicApplicationTheme {
                val navController = rememberNavController()
                Scaffold(
                    containerColor = AppTheme.colors.backgroundColor,
                    topBar = {
                        MainAppBar(navController)
                    }
                ) {
                        padding ->
                    Row(
                        Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .consumeWindowInsets(padding)
                            .windowInsetsPadding(
                                WindowInsets.safeDrawing.only(
                                    WindowInsetsSides.Horizontal,
                                ),
                            ),
                    )
                    {
                        val systemUiController = rememberSystemUiController()
                        val primaryBackground = AppTheme.colors.secondaryBackgroundColor
                        SideEffect {
                            systemUiController.setSystemBarsColor(
                                color = primaryBackground,
                                darkIcons = true
                            )
                        }
                        ApplicationScreen(navController)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainAppBar(navController: NavHostController) {
    val currentRoute = remember { mutableStateOf(navController.currentBackStackEntry?.destination?.route) }

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            currentRoute.value = destination.route
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }


    TopAppBar(
        title = {
            Text(textResource(id = R.string.app_name).toString(),
            color = White)
            },
        navigationIcon = {
            if (currentRoute.value?.contains(NavigationTree.Details.name) == true) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = AppTheme.colors.secondaryBackgroundColor)
    )
}

@Composable
@ReadOnlyComposable
fun textResource(@StringRes id: Int): CharSequence =
    LocalContext.current.resources.getText(id)