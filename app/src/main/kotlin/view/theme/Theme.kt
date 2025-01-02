package view.theme

import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val LightColorScheme = lightColors(
    background = Color(0xFFf9f9f9), // background
    primary = Gray700, // textNormal and iconNormal
    onPrimary = Color.White ,// selected item
    primaryVariant = Gray400,  // dividerNormal and buttonSilverBackground
    secondary = Color.Black // textStrong and dividerStrong
)