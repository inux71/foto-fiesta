package com.xdteam.fotofiesta

sealed class Screen(val route: String) {
    object PreviewScreen : Screen(route = "preview_screen")
    object SettingsScreen : Screen(route = "settings_screen")
    object PDFScreen : Screen(route = "pdf_screen")
}
