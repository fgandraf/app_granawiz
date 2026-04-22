package view.modules

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import application.userPreference.UserPreferenceHandler

object UserPreferences {
    var isLightTheme by mutableStateOf(true)

    fun loadFromDatabase() {
        val prefs = UserPreferenceHandler().fetchPreferences()
        isLightTheme = prefs.isLightTheme
    }
}