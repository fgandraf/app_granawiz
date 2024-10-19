package ui.theme

import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val LightColorScheme = lightColors(
    background = Color.White, // background
    primary = Gray700, // textNormal and iconNormal
    primaryVariant = Gray400,  // dividerNormal and buttonSilverBackground
    secondary = Color.Black, // textStrong and dividerStrong
    onPrimary = Indigo900 // selected item
)