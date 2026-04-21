package domain.userPreference

import core.entity.UserPreference
import domain.userPreference.usecases.FetchUserPreferenceUseCase
import domain.userPreference.usecases.UpdateUserPreferenceUseCase

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
