package ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font

val Inter = FontFamily(
    fonts = listOf(
        Font(
            resource = "fonts/inter-semibold.ttf",
            weight = FontWeight.SemiBold,
            style = FontStyle.Normal
        ),
        Font(
            resource = "fonts/inter-regular.ttf",
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        )
    )
)