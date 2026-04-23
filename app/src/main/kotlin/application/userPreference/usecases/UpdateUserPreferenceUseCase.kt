package application.userPreference.usecases

import domain.contracts.IUserPreferenceRepository
import domain.entity.UserPreference
import infrastructure.repository.UserPreferenceRepository

class UpdateUserPreferenceUseCase(private val dao: IUserPreferenceRepository = UserPreferenceRepository()) {

    fun execute(userPreference: UserPreference) {
        dao.update(userPreference)
    }
}
