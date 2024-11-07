import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import config.DatabaseConfig
import view.modules.dashboard.DashboardScreen
import view.modules.sidebar.Sidebar
import view.theme.LightColorScheme
import java.awt.Toolkit
import java.util.*


fun main() = application {

    DatabaseConfig.runMigrations()

    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val windowsWidth = (screenSize.width * 0.8).toInt().dp
    val windowsHeight = (screenSize.height * 0.8).toInt().dp

    Locale.setDefault(Locale.forLanguageTag("pt-BR"))

    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(width = windowsWidth, height = windowsHeight, position = WindowPosition.Aligned(Alignment.Center)),
        title = "Money Map"
    ) {
        MaterialTheme(colors = LightColorScheme) {

            Row(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
                Sidebar()
                DashboardScreen()
            }
        }
    }
}