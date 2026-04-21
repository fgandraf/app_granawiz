package infra.dao

import core.contracts.IUserPreferenceDao
import core.entity.UserPreference
import infra.config.HibernateUtil

class UserPreferenceDao : IUserPreferenceDao {

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
