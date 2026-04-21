package viewModel

import domain.userPreference.UserPreferenceHandler
import view.modules.UserPreferences

class SettingsViewModel(private val handler: UserPreferenceHandler = UserPreferenceHandler()) {

    fun setTheme(isLightTheme: Boolean) {
        UserPreferences.isLightTheme = isLightTheme
        handler.updateTheme(isLightTheme)
    }
}
