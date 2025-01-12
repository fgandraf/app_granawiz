package view.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color


val GrayPrimaryLight = Color(0xFF565656)
val GrayPrimaryDark = Color(0xFF9b9b9b)
val RedWarning = Color(0xFFFF6961)
val ButtonGreen = Color(0xFF8BB581)
val ButtonPurple = Color(0xFF73378a)


val LightColorScheme = lightColors(
    primary = GrayPrimaryLight, // text
    primaryVariant = Color(0xFFa6a6a6), //lighter text
    secondary = Color(0xFF000000), //emphasis text
    secondaryVariant = Color(0xFFEEEEEE), // lightest gray
    background = Color(0xFFf8f8f7), // screen background
    surface = Color(0xFFFFFFFF), // sidebar
    onSurface = Color(0xFFbfbfbf), // borders
    onError = Color(0xFFEF5350), // expense red
    onPrimary = Color(0xFF06ca38) // income green
)


val DarkColorScheme = darkColors(
    primary = Color(0xFF9b9b9b), // text *********
    primaryVariant = Color(0xFF686868), //lighter text  *******
    secondary = Color(0xFFd5d5d5), //emphasis text *************
    secondaryVariant = Color(0xFF121212), // darkest gray
    background = Color(0xFF202020), // screen background ***********
    surface = Color(0xFF191919), // sidebar ************
    onSurface = Color(0xFF303030), // borders
    onError = Color(0xFFd0484b), // expense red
    onPrimary = Color(0xFF007000) // income green
)