package dev.danya.museum

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.danya.museum.core.ui.theme.MuseumTheme
import dev.danya.museum.navigation.RootNavHost

@Composable
@Preview
fun App() {
    MuseumTheme {
        RootNavHost()
    }
}