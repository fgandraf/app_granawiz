package infrastructure.repository

import domain.contracts.IUserPreferenceRepository
import domain.entity.UserPreference
import infrastructure.config.HibernateUtil

class UserPreferenceRepository : IUserPreferenceRepository {

    private val sessionFactory = HibernateUtil.getSessionFactory()

    override fun get(): UserPreference {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val preference = session.get(UserPreference::class.java, 1L)
        session.transaction.commit()
        session.close()
        return preference ?: UserPreference()
    }

    override fun update(userPreference: UserPreference) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.merge(userPreference)
        session.transaction.commit()
        session.close()
    }
}
