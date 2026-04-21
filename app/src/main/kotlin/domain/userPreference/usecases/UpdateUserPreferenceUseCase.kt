package domain.userPreference.usecases

import core.entity.UserPreference
import infra.dao.UserPreferenceDao

class UpdateUserPreferenceUseCase(private val dao: UserPreferenceDao = UserPreferenceDao()) {

    fun execute(userPreference: UserPreference) {
        dao.update(userPreference)
    }
}
