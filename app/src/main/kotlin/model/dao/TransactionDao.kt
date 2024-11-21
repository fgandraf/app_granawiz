package model.dao

import config.HibernateUtil
import model.entity.Transaction
import org.hibernate.Hibernate

class TransactionDao {

    private val sessionFactory = HibernateUtil.getSessionFactory()

    fun getAll() : List<Transaction> {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val criteriaBuilder = session.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(Transaction::class.java)
        val root = criteriaQuery.from(Transaction::class.java)
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get<String>("description")))
        val transactions = session.createQuery(criteriaQuery).resultList
        transactions.forEach { transaction ->
            Hibernate.initialize(transaction.party)
            Hibernate.initialize(transaction.account)
            Hibernate.initialize(transaction.category)
            Hibernate.initialize(transaction.subcategory)
            Hibernate.initialize(transaction.tags)
        }
        session.transaction.commit()
        session.close()
        return transactions
    }


//    fun delete(group: Group) {
//        val session = sessionFactory.openSession()
//
//        session.beginTransaction()
//        session.remove(group)
//        session.transaction.commit()
//
//        session.close()
//    }
//
//
//
//    fun updateGroupPositions(accounts: List<Group>) {
//        accounts.forEach { group ->
//            val session = sessionFactory.openSession()
//            session.beginTransaction()
//            session.merge(group)
//            session.transaction.commit()
//            session.close()
//        }
//    }
//
//    fun update(group: Group) {
//        val session = sessionFactory.openSession()
//        session.beginTransaction()
//        session.merge(group)
//        session.transaction.commit()
//        session.close()
//    }
//
//    fun insert(group: Group) {
//        val session = sessionFactory.openSession()
//        session.beginTransaction()
//        session.persist(group)
//        session.transaction.commit()
//        session.close()
//    }

}