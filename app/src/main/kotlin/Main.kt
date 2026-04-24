import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import infrastructure.config.DatabaseConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import view.modules.CustomTitleBar
import view.modules.MainContent
import view.modules.Screen
import view.modules.SplashWindow
import view.modules.UserPreferences
import view.modules.sidebar.Sidebar
import view.theme.DarkColorScheme
import view.theme.LightColorScheme
import java.awt.Toolkit
import java.awt.geom.RoundRectangle2D
import java.util.*
import kotlin.time.Duration.Companion.milliseconds


fun main() = application {

    Locale.setDefault(Locale.forLanguageTag("pt-BR"))

    var isReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            DatabaseConfig.runMigrations()
            UserPreferences.loadFromDatabase()
        }
        delay(1000.milliseconds)
        isReady = true
    }

    if (isReady) {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val windowsWidth = (screenSize.width * 0.80).toInt().dp
        val windowsHeight = (screenSize.height * 0.80).toInt().dp

        val windowState = WindowState(
            width = windowsWidth,
            height = windowsHeight,
            position = WindowPosition.Aligned(Alignment.Center)
        )

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "GranaWiz",
            undecorated = true
        ) {

            LaunchedEffect(windowState.size, windowState.placement) {
                if (windowState.placement == WindowPlacement.Maximized) {
                    window.shape = null
                } else {
                    val w = windowState.size.width.value.toDouble()
                    val h = windowState.size.height.value.toDouble()
                    window.shape = RoundRectangle2D.Double(0.0, 0.0, w, h, 25.0, 25.0)
                }
            }

            val isLightTheme = UserPreferences.isLightTheme
            val currentColorScheme = if (isLightTheme) LightColorScheme else DarkColorScheme

            MaterialTheme(colors = currentColorScheme) {

                var currentScreen by remember { mutableStateOf<Screen>(Screen.Dashboard) }

                Column(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp))) {
                    CustomTitleBar(
                        windowState = windowState,
                        onCloseRequest = ::exitApplication
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .background(MaterialTheme.colors.background)
                    ) {
                        Sidebar(currentScreen = currentScreen) { screen -> currentScreen = screen }
                        MainContent(currentScreen)
                    }
                }

            }
        }
    } else {
        SplashWindow()
    }
}