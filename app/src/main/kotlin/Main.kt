import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import infrastructure.config.DatabaseConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.flywaydb.core.api.exception.FlywayValidateException
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
    var dbError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                DatabaseConfig.runMigrations()
                UserPreferences.loadFromDatabase()
            } catch (_: FlywayValidateException) {
                dbError = AppConfig.dbAbsolutePath
                return@withContext
            }
        }
        if (dbError == null) {
            delay(1000.milliseconds)
            isReady = true
        }
    }

    if (dbError != null) {
        Window(
            onCloseRequest = ::exitApplication,
            undecorated = true,
            alwaysOnTop = true,
            resizable = false,
            state = WindowState(
                width = 520.dp,
                height = 260.dp,
                position = WindowPosition.Aligned(Alignment.Center)
            ),
            title = "GranaWiz"
        ) {
            LaunchedEffect(Unit) {
                window.shape = RoundRectangle2D.Double(0.0, 0.0, 520.0, 260.0, 20.0, 20.0)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1E1E2E))
                    .padding(32.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Erro ao inicializar o banco de dados",
                        color = Color(0xFFFF6B6B),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "O banco de dados está incompatível com esta versão do GranaWiz.",
                        color = Color.White,
                        fontSize = 13.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Para corrigir, exclua o arquivo abaixo e reinicie o aplicativo:",
                        color = Color(0xFFAAAAAA),
                        fontSize = 13.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = dbError!!,
                        color = Color(0xFFFFD700),
                        fontSize = 12.sp
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = ::exitApplication) {
                        Text("Fechar")
                    }
                }
            }
        }
        return@application
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