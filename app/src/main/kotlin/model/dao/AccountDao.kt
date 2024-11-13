package model.dao

import config.HibernateUtil
import model.entity.account.BankAccount

class AccountDao {

    private val sessionFactory = HibernateUtil.getSessionFactory()

    fun insert(account: BankAccount) {
        val session = sessionFactory.openSession()

        session.beginTransaction()
        session.persist(account)
        session.transaction.commit()

        session.close()
    }

    fun update(account: BankAccount) {
        val session = sessionFactory.openSession()

        session.beginTransaction()
        session.merge(account)
        session.transaction.commit()

        session.close()
    }

    fun delete(account: BankAccount) {
        val session = sessionFactory.openSession()

        session.beginTransaction()
        session.remove(account)
        session.transaction.commit()

        session.close()
    }

    fun updateAccountPositions(accounts: List<BankAccount>) {
        accounts.forEach { account ->
            val session = sessionFactory.openSession()
            session.beginTransaction()
            session.merge(account)
            session.transaction.commit()
            session.close()
        }
    }


}