package application.userPreference.usecases

import domain.entity.UserPreference
import infrastructure.repository.UserPreferenceRepository

class FetchUserPreferenceUseCase(private val dao: UserPreferenceRepository = UserPreferenceRepository()) {

    fun execute(): UserPreference {
        return dao.get()
    }
}
