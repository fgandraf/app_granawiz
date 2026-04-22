package application.userPreference.usecases

import domain.entity.UserPreference
import infrastructure.repository.UserPreferenceRepository

class UpdateUserPreferenceUseCase(private val dao: UserPreferenceRepository = UserPreferenceRepository()) {

    fun execute(userPreference: UserPreference) {
        dao.update(userPreference)
    }
}
