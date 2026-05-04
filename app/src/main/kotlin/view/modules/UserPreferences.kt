package view.modules

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import application.userPreference.UserPreferenceHandler

object UserPreferences {
    var isLightTheme by mutableStateOf(true)
    var currencyLabel by mutableStateOf("Brazilian Real (R$)")
    var currencyFormat by mutableStateOf("dot-comma")
    var language by mutableStateOf("pt-BR")
    var titleBarStyle by mutableStateOf("default")

    // Derived reactively from currencyLabel — triggers recomposition in any Composable that reads it
    val currencySymbol: String
        get() {
            val stored = currencyLabel
            return if (stored.contains("(")) stored.substringAfterLast("(").removeSuffix(")")
            else stored
        }

    fun loadFromDatabase() {
        val prefs = UserPreferenceHandler().fetchPreferences()
        isLightTheme = prefs.isLightTheme
        currencyLabel = prefs.currencySymbol
        currencyFormat = prefs.currencyFormat
        language = prefs.language
        titleBarStyle = prefs.titleBarStyle
    }
}