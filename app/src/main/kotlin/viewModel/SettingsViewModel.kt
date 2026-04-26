package viewModel

import application.userPreference.UserPreferenceHandler
import view.modules.UserPreferences

class SettingsViewModel(private val handler: UserPreferenceHandler = UserPreferenceHandler()) {

    fun setTheme(isLightTheme: Boolean) {
        UserPreferences.isLightTheme = isLightTheme
        handler.updateTheme(isLightTheme)
    }

    fun setCurrencySymbol(label: String) {
        UserPreferences.currencyLabel = label
        handler.updateCurrencySymbol(label)
    }

    fun setCurrencyFormat(format: String) {
        UserPreferences.currencyFormat = format
        handler.updateCurrencyFormat(format)
    }
}
