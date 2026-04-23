import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import infrastructure.config.DatabaseConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import view.modules.MainContent
import view.modules.Screen
import view.modules.SplashWindow
import view.modules.UserPreferences
import view.modules.sidebar.Sidebar
import view.theme.DarkColorScheme
import view.theme.LightColorScheme
import java.awt.Toolkit
import java.util.*


fun main() = application {

    Locale.setDefault(Locale.forLanguageTag("pt-BR"))

    var isReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            DatabaseConfig.runMigrations()
            UserPreferences.loadFromDatabase()
        }
        isReady = true
    }

    if (isReady) {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val windowsWidth = (screenSize.width * 0.80).toInt().dp
        val windowsHeight = (screenSize.height * 0.80).toInt().dp

        Window(
            onCloseRequest = ::exitApplication,
            state = WindowState(
                width = windowsWidth,
                height = windowsHeight,
                position = WindowPosition.Aligned(Alignment.Center)
            ),
            title = "GranaWiz"
        ) {
            val isLightTheme = UserPreferences.isLightTheme
            val currentColorScheme = if (isLightTheme) LightColorScheme else DarkColorScheme

            MaterialTheme(colors = currentColorScheme) {

                var currentScreen by remember { mutableStateOf<Screen>(Screen.Dashboard) }

                Row(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
                    Sidebar(currentScreen = currentScreen) { screen -> currentScreen = screen }
                    MainContent(currentScreen)
                }

            }
        }
    } else {
        SplashWindow()
    }
}