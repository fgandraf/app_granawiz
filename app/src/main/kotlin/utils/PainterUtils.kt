package utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadSvgPainter

// Dynamic SVG loading by runtime string path has no replacement in the new
// Compose resources API (which requires compile-time DrawableResource references).
@Suppress("DEPRECATION")
@Composable
fun rememberSvgPainter(resourcePath: String): Painter {
    val density = LocalDensity.current
    return remember(resourcePath, density) {
        Thread.currentThread().contextClassLoader
            .getResourceAsStream(resourcePath)!!
            .use { loadSvgPainter(it, density) }
    }
}
