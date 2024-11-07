package domain.contracts

import domain.entity.UserPreference

interface IUserPreferenceRepository {
    fun get(): UserPreference
    fun update(userPreference: UserPreference)
}
