package infra.dao

import core.contracts.IAccountDao
import core.entity.account.BankAccount
import infra.config.HibernateUtil

class AccountDao : IAccountDao {

    private val sessionFactory = HibernateUtil.getSessionFactory()

    override fun insert(account: BankAccount) {
        val session = sessionFactory.openSession()

        session.beginTransaction()
        session.persist(account)
        session.transaction.commit()

        session.close()
    }

    override fun update(account: BankAccount) {
        val session = sessionFactory.openSession()

        session.beginTransaction()
        session.merge(account)
        session.transaction.commit()

        session.close()
    }

    override fun delete(account: BankAccount) {
        val session = sessionFactory.openSession()

        session.beginTransaction()
        session.remove(account)
        session.transaction.commit()

        session.close()
    }

    override fun updateAccountPositions(accounts: List<BankAccount>) {
        accounts.forEach { account ->
            val session = sessionFactory.openSession()
            session.beginTransaction()
            session.merge(account)
            session.transaction.commit()
            session.close()
        }
    }

    override fun getAccountById(id: Long): BankAccount? {
        val session = sessionFactory.openSession()
        val account = session.get(BankAccount::class.java, id)
        session.close()
        return account
    }


}