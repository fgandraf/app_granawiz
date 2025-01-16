package view.modules

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object UserPreferences {
    var isLightTheme by mutableStateOf(false)
}