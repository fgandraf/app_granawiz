package domain.userPreference.usecases

import core.entity.UserPreference
import infra.dao.UserPreferenceDao

class FetchUserPreferenceUseCase(private val dao: UserPreferenceDao = UserPreferenceDao()) {

    fun execute(): UserPreference {
        return dao.get()
    }
}
