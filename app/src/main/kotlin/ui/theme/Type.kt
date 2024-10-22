package ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font

val Afacade = FontFamily(
    fonts = listOf(
        Font(
            resource = "fonts/afacad-regular.ttf",
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        )
    )
)

val Ubuntu = FontFamily(
    fonts = listOf(
        Font(
            resource = "fonts/ubuntu-regular.ttf",
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),
        Font(
            resource = "fonts/ubuntu-medium.ttf",
            weight = FontWeight.Medium,
            style = FontStyle.Normal
        ),
        Font(
            resource = "fonts/ubuntu-bold.ttf",
            weight = FontWeight.SemiBold,
            style = FontStyle.Normal
        )
    )
)