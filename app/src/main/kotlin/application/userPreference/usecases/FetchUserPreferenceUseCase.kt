package application.userPreference.usecases

import domain.contracts.IUserPreferenceRepository
import domain.entity.UserPreference
import infrastructure.repository.UserPreferenceRepository

class FetchUserPreferenceUseCase(private val dao: IUserPreferenceRepository = UserPreferenceRepository()) {

    fun execute(): UserPreference {
        return dao.get()
    }
}
