package model.dao

import config.HibernateUtil
import model.entity.Group
import org.hibernate.Hibernate

class GroupDao {

    private val sessionFactory = HibernateUtil.getSessionFactory()

    fun getAll() : List<Group> {
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


    fun delete(group: Group) {
        val session = sessionFactory.openSession()

        session.beginTransaction()
        session.remove(group)
        session.transaction.commit()

        session.close()
    }



    fun updateGroupPositions(accounts: List<Group>) {
        accounts.forEach { group ->
            val session = sessionFactory.openSession()
            session.beginTransaction()
            session.merge(group)
            session.transaction.commit()
            session.close()
        }
    }

    fun update(group: Group) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.merge(group)
        session.transaction.commit()
        session.close()
    }

    fun insert(group: Group) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.persist(group)
        session.transaction.commit()
        session.close()
    }

}