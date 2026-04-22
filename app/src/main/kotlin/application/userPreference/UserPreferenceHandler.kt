package application.userPreference

import domain.entity.UserPreference
import application.userPreference.usecases.FetchUserPreferenceUseCase
import application.userPreference.usecases.UpdateUserPreferenceUseCase

class UserPreferenceHandler {
    private val fetchUseCase = FetchUserPreferenceUseCase()
    private val updateUseCase = UpdateUserPreferenceUseCase()

    fun fetchPreferences(): UserPreference = fetchUseCase.execute()

    fun updateTheme(isLightTheme: Boolean) {
        val prefs = fetchUseCase.execute()
        prefs.isLightTheme = isLightTheme
        updateUseCase.execute(prefs)
    }
}
