package com.roksidark.musicapplication.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.roksidark.musicapplication.R
import com.roksidark.musicapplication.presentation.theme.AppTheme
import com.roksidark.musicapplication.presentation.theme.MusicApplicationTheme
import com.roksidark.musicapplication.presentation.ui.screen.ApplicationScreen
import com.roksidark.musicapplication.presentation.ui.screen.textResource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicApplicationTheme {
                Scaffold(
                    containerColor = AppTheme.colors.backgroundColor,
                    topBar = {
                        MainAppBar()
                    }
                ) {
                    val systemUiController = rememberSystemUiController()
                    val primaryBackground = AppTheme.colors.secondaryBackgroundColor
                    SideEffect {
                        systemUiController.setSystemBarsColor(
                            color = primaryBackground,
                            darkIcons = true
                        )
                    }
                    ApplicationScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainAppBar() {
    TopAppBar(
        title = { Text(textResource(id = R.string.app_name).toString(),
            color = AppTheme.colors.headerTextColor) })
}