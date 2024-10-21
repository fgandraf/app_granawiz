package ui.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font

val AppFont = FontFamily(
    fonts = listOf(
        Font(
            resource = "fonts/poppins-regular.ttf",
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),
        Font(
            resource = "fonts/poppins-medium.ttf",
            weight = FontWeight.Medium,
            style = FontStyle.Normal
        ),
        Font(
            resource = "fonts/poppins-semibold.ttf",
            weight = FontWeight.SemiBold,
            style = FontStyle.Normal
        )
    )
)