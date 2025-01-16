import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import infra.config.DatabaseConfig
import view.modules.MainContent
import view.modules.Screen
import view.modules.UserPreferences
import view.modules.sidebar.Sidebar
import view.theme.DarkColorScheme
import view.theme.LightColorScheme
import java.awt.Toolkit
import java.util.*


fun main() = application {

    DatabaseConfig.runMigrations()

    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val windowsWidth = (screenSize.width * 0.80).toInt().dp
    val windowsHeight = (screenSize.height * 0.80).toInt().dp

    Locale.setDefault(Locale.forLanguageTag("pt-BR"))

    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(width = windowsWidth, height = windowsHeight, position = WindowPosition.Aligned(Alignment.Center)),
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
}