package utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
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
            .getResourceAsStream(resourcePath)
            ?.use { loadSvgPainter(it, density) }
            ?: ColorPainter(Color.Transparent)
    }
}

fun isCustomIcon(icon: String): Boolean = icon.startsWith("CUSTOM|")

fun parseCustomIcon(icon: String): Pair<String, String> {
    val withoutPrefix = icon.removePrefix("CUSTOM|")
    val idx = withoutPrefix.indexOf('|')
    return if (idx >= 0) withoutPrefix.substring(0, idx) to withoutPrefix.substring(idx + 1)
    else withoutPrefix to ""
}

@Suppress("DEPRECATION")
@Composable
fun rememberAccountIconPainter(icon: String, iconSvg: String?): Painter {
    val density = LocalDensity.current
    return remember(icon, iconSvg, density) {
        if (iconSvg != null) {
            iconSvg.byteInputStream().use { loadSvgPainter(it, density) }
        } else {
            val resourcePath = if (isCustomIcon(icon)) IconPaths.BANK_LOGOS + "_default.svg"
                               else IconPaths.BANK_LOGOS + icon
            Thread.currentThread().contextClassLoader
                .getResourceAsStream(resourcePath)
                ?.use { loadSvgPainter(it, density) }
                ?: ColorPainter(Color.Transparent)
        }
    }
}
