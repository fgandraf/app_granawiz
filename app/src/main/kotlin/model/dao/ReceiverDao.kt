package model.dao

import config.HibernateUtil
import model.entity.Receiver
import org.hibernate.Hibernate

class ReceiverDao {

    private val sessionFactory = HibernateUtil.getSessionFactory()

    fun getAll() : List<Receiver> {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val criteriaBuilder = session.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(Receiver::class.java)
        val root = criteriaQuery.from(Receiver::class.java)
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get<Int>("name")))
        val receivers = session.createQuery(criteriaQuery).resultList
        receivers.forEach { receiver ->
            Hibernate.initialize(receiver.receiverNames)
            receiver.receiverNames.sortBy { it.name }
        }
        session.transaction.commit()
        session.close()
        return receivers
    }


    fun delete(receiver: Receiver) {
        val session = sessionFactory.openSession()

        session.beginTransaction()
        session.remove(receiver)
        session.transaction.commit()

        session.close()
    }

    fun update(receiver: Receiver) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.merge(receiver)
        session.transaction.commit()
        session.close()
    }

    fun insert(receiver: Receiver) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.persist(receiver)
        session.transaction.commit()
        session.close()
    }
}