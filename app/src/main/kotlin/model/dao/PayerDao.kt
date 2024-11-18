package model.dao

import config.HibernateUtil
import model.entity.Payer
import model.entity.PayerName

class PayerDao {

    private val sessionFactory = HibernateUtil.getSessionFactory()

    fun getAll() : List<Payer> {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val criteriaBuilder = session.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(Payer::class.java)
        val root = criteriaQuery.from(Payer::class.java)
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get<Int>("name")))
        val payers = session.createQuery(criteriaQuery).resultList
        payers.forEach { payer -> payer.payersNames.sortBy { it.name } }
        session.transaction.commit()
        session.close()
        return payers
    }

    fun getPayerNameByName(name: String): PayerName? {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val criteriaBuilder = session.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(PayerName::class.java)
        val root = criteriaQuery.from(PayerName::class.java)
        criteriaQuery.where(criteriaBuilder.equal(root.get<String>("name"), name))
        val query = session.createQuery(criteriaQuery)
        val payerName = query.resultList.firstOrNull()
        session.transaction.commit()
        session.close()
        return payerName
    }


    fun delete(payer: Payer) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.remove(payer)
        session.transaction.commit()
        session.close()
    }

    fun deleteName(payerName: PayerName) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.remove(payerName)
        session.transaction.commit()
        session.close()
    }

    fun update(payer: Payer) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.merge(payer)
        session.transaction.commit()
        session.close()
    }

    fun updateName(payerName: PayerName) {
        val session = sessionFactory.openSession()
        try {
            session.beginTransaction()
            session.merge(payerName)
            session.transaction.commit()
        } catch (e: Exception) {
            session.transaction.rollback()
            if (e.cause is org.hibernate.exception.ConstraintViolationException)
                println("Erro: O nome '${payerName.name}' já está associado ao pagador '${payerName.payer.name}'.")
        } finally {
            session.close()
        }
    }

    fun insert(payer: Payer) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.persist(payer)
        session.transaction.commit()
        session.close()
    }

    fun insertName(payerName: PayerName) {
        val session = sessionFactory.openSession()
        try {
            session.beginTransaction()
            session.persist(payerName)
            session.transaction.commit()
        } catch (e: Exception) {
            session.transaction.rollback()
            if (e.cause is org.hibernate.exception.ConstraintViolationException)
                println("Erro: O nome '${payerName.name}' já está associado ao pagador '${payerName.payer.name}'.")
        } finally {
            session.close()
        }
    }
}