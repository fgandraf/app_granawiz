package view.modules

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.Alignment
import java.awt.geom.RoundRectangle2D

@Suppress("DEPRECATION")
@Composable
fun SplashWindow() {
    Window(
        onCloseRequest = {},
        undecorated = true,
        alwaysOnTop = true,
        resizable = false,
        state = WindowState(
            width = 688.dp,
            height = 384.dp,
            position = WindowPosition.Aligned(Alignment.Center)
        ),
        title = "GranaWiz"
    ) {
        LaunchedEffect(Unit) {
            window.shape = RoundRectangle2D.Double(0.0, 0.0, 688.0, 384.0, 20.0, 20.0)
        }

        Image(
            painter = painterResource("assets/images/splash.png"),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.FillBounds
        )
    }
}
