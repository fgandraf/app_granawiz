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

    fun updateCurrencySymbol(symbol: String) {
        val prefs = fetchUseCase.execute()
        prefs.currencySymbol = symbol
        updateUseCase.execute(prefs)
    }

    fun updateCurrencyFormat(format: String) {
        val prefs = fetchUseCase.execute()
        prefs.currencyFormat = format
        updateUseCase.execute(prefs)
    }

    fun updateLanguage(language: String) {
        val prefs = fetchUseCase.execute()
        prefs.language = language
        updateUseCase.execute(prefs)
    }
}
