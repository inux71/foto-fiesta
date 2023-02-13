package com.xdteam.fotofiesta

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.xdteam.fotofiesta.presentation.pdf_screen.PDFScreen
import com.xdteam.fotofiesta.presentation.preview_screen.PreviewScreen
import com.xdteam.fotofiesta.presentation.settings_screen.SettingsPage

@Composable
fun NavGraph(navHostController: NavHostController, snackbarHostState: SnackbarHostState) {
    NavHost(navController = navHostController, startDestination = Screen.PDFScreen.route) {
        composable(Screen.PreviewScreen.route) {
            PreviewScreen(
                onSettingsClick = {
                    navHostController.navigate(Screen.SettingsScreen.route)
                },
            )
        }

        composable(Screen.SettingsScreen.route) {
            SettingsPage(
                onBackClick = {
                    navHostController.popBackStack()
                }
            )
        }

        composable(Screen.PDFScreen.route) {
            PDFScreen()
        }
    }
}
