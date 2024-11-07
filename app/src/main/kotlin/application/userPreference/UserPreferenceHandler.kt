package application.userPreference

import domain.contracts.IUserPreferenceRepository
import domain.entity.UserPreference

class UserPreferenceHandler(private val dao: IUserPreferenceRepository) {

    fun fetchPreferences(): UserPreference = dao.get()

    fun updateTheme(isLightTheme: Boolean) {
        val prefs = dao.get()
        prefs.isLightTheme = isLightTheme
        dao.update(prefs)
    }

    fun updateCurrencySymbol(symbol: String) {
        val prefs = dao.get()
        prefs.currencySymbol = symbol
        dao.update(prefs)
    }

    fun updateCurrencyFormat(format: String) {
        val prefs = dao.get()
        prefs.currencyFormat = format
        dao.update(prefs)
    }

    fun updateLanguage(language: String) {
        val prefs = dao.get()
        prefs.language = language
        dao.update(prefs)
    }

    fun updateTitleBarStyle(style: String) {
        val prefs = dao.get()
        prefs.titleBarStyle = style
        dao.update(prefs)
    }
}
