package infra.dao

import core.contracts.IGroupDao
import core.entity.Group
import infra.config.HibernateUtil
import org.hibernate.Hibernate

class GroupDao : IGroupDao {

    private val sessionFactory = HibernateUtil.getSessionFactory()

    override fun getAll() : List<Group> {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val criteriaBuilder = session.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(Group::class.java)
        val root = criteriaQuery.from(Group::class.java)
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get<Int>("position")))
        val grupo = session.createQuery(criteriaQuery).resultList
        grupo.forEach { group ->
            Hibernate.initialize(group.accounts)
            group.accounts.sortBy { it.position }
        }
        session.transaction.commit()
        session.close()
        return grupo
    }


    override fun delete(group: Group) {
        val session = sessionFactory.openSession()

        session.beginTransaction()
        session.remove(group)
        session.transaction.commit()

        session.close()
    }



    override fun updateGroupPositions(accounts: List<Group>) {
        accounts.forEach { group ->
            val session = sessionFactory.openSession()
            session.beginTransaction()
            session.merge(group)
            session.transaction.commit()
            session.close()
        }
    }

    override fun update(group: Group) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.merge(group)
        session.transaction.commit()
        session.close()
    }

    override fun insert(group: Group) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.persist(group)
        session.transaction.commit()
        session.close()
    }

}