package view.theme

import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val LightColorScheme = lightColors(
    background = NotionGray, // background
    primary = Gray700, // textNormal and iconNormal
    onPrimary = Color.White ,// selected item
    primaryVariant = Gray400,  // dividerNormal and buttonSilverBackground
    secondary = Color.Black // textStrong and dividerStrong
)