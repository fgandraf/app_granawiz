package model.dao

import config.HibernateUtil
import model.entity.ReceiverName
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

    fun getReceiverNameByName(name: String): ReceiverName? {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val criteriaBuilder = session.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(ReceiverName::class.java)
        val root = criteriaQuery.from(ReceiverName::class.java)
        criteriaQuery.where(criteriaBuilder.equal(root.get<String>("name"), name))
        val query = session.createQuery(criteriaQuery)
        val receiverName = query.resultList.firstOrNull()
        session.transaction.commit()
        session.close()
        return receiverName
    }


    fun delete(receiver: Receiver) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.remove(receiver)
        session.transaction.commit()
        session.close()
    }

    fun deleteName(receiveName: ReceiverName) {
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

    fun updateName(receiverName: ReceiverName) {
        val session = sessionFactory.openSession()
        try {
            session.beginTransaction()
            session.merge(receiverName)
            session.transaction.commit()
        } catch (e: Exception) {
            session.transaction.rollback()
            if (e.cause is org.hibernate.exception.ConstraintViolationException)
                println("Erro: O nome '${receiverName.name}' já está associado ao recebedor '${receiverName.receiver.name}'.")
        } finally {
            session.close()
        }
    }

    fun insert(receiver: Receiver) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.persist(receiver)
        session.transaction.commit()
        session.close()
    }

    fun insertName(receiverName: ReceiverName) {
        val session = sessionFactory.openSession()
        try {
            session.beginTransaction()
            session.persist(receiverName)
            session.transaction.commit()
        } catch (e: Exception) {
            session.transaction.rollback()
            if (e.cause is org.hibernate.exception.ConstraintViolationException)
                println("Erro: O nome '${receiverName.name}' já está associado ao recebedor '${receiverName.receiver.name}'.")
        } finally {
            session.close()
        }
    }
}