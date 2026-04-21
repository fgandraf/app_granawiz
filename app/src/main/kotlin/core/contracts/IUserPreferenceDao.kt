package core.contracts

import core.entity.UserPreference

interface IUserPreferenceDao {
    fun get(): UserPreference
    fun update(userPreference: UserPreference)
}
