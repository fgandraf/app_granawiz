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
        session.beginTransaction()
        val criteriaBuilder = session.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(BankAccount::class.java)
        val root = criteriaQuery.from(BankAccount::class.java)
        criteriaQuery.where(criteriaBuilder.equal(root.get<Long>("id"), id))
        val query = session.createQuery(criteriaQuery)
        val account = query.resultList.firstOrNull()
        session.transaction.commit()
        session.close()


        return account
    }


}