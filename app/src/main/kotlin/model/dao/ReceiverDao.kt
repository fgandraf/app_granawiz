package model.dao

import config.HibernateUtil
import model.entity.AssociatedReceiverName
import model.entity.Receiver

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
        receivers.forEach { receiver -> receiver.receiverNames.sortBy { it.name } }
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

    fun deleteName(receiveName: AssociatedReceiverName) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.remove(receiveName)
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