package view.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font

val Afacade = FontFamily(
    fonts = listOf(
        Font(
            resource = "assets/fonts/afacad-regular.ttf",
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        )
    )
)

val DefaultFont = FontFamily(
    fonts = listOf(
        Font(
            resource = "assets/fonts/inter-regular.ttf",
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),
        Font(
            resource = "assets/fonts/inter-medium.ttf",
            weight = FontWeight.Medium,
            style = FontStyle.Normal
        ),
        Font(
            resource = "assets/fonts/inter-bold.ttf",
            weight = FontWeight.SemiBold,
            style = FontStyle.Normal
        )
    )
)