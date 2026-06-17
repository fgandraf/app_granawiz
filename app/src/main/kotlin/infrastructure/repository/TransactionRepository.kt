package infrastructure.repository

import domain.contracts.ITransactionRepository
import domain.entity.Category
import domain.entity.Subcategory
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.enums.TransactionType
import infrastructure.config.HibernateUtil
import java.time.LocalDateTime

class TransactionRepository : ITransactionRepository {

    private val sessionFactory = HibernateUtil.getSessionFactory()

    override fun getAll(): List<Transaction> {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val transactions = session.createQuery(
            """SELECT DISTINCT t FROM Transaction t
               LEFT JOIN FETCH t.party LEFT JOIN FETCH t.account
               LEFT JOIN FETCH t.category LEFT JOIN FETCH t.subcategory
               LEFT JOIN FETCH t.tags
               ORDER BY t.date DESC""",
            Transaction::class.java,
        ).resultList
        session.transaction.commit()
        session.close()
        return transactions
    }

    override fun getByDateRange(
        from: LocalDateTime,
        to: LocalDateTime,
        type: TransactionType?,
        excludeTransfers: Boolean,
    ): List<Transaction> {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val typeClause = if (type != null) " AND t.type = :type" else ""
        val transferClause = if (excludeTransfers) " AND t.isTransfer = false" else ""
        val query = session.createQuery(
            """SELECT DISTINCT t FROM Transaction t
               LEFT JOIN FETCH t.party LEFT JOIN FETCH t.account
               LEFT JOIN FETCH t.category LEFT JOIN FETCH t.subcategory
               LEFT JOIN FETCH t.tags
               WHERE t.date BETWEEN :from AND :to$typeClause$transferClause
               ORDER BY t.date DESC""",
            Transaction::class.java,
        ).setParameter("from", from).setParameter("to", to)
        if (type != null) query.setParameter("type", type)
        val transactions = query.resultList
        session.transaction.commit()
        session.close()
        return transactions
    }

    override fun getAllByAccount(account: BankAccount): List<Transaction> {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val transactions = session.createQuery(
            """SELECT DISTINCT t FROM Transaction t
               LEFT JOIN FETCH t.party LEFT JOIN FETCH t.account
               LEFT JOIN FETCH t.category LEFT JOIN FETCH t.subcategory
               LEFT JOIN FETCH t.tags
               WHERE t.account = :account
               ORDER BY t.date DESC""",
            Transaction::class.java,
        ).setParameter("account", account).resultList
        session.transaction.commit()
        session.close()
        return transactions
    }

    override fun getAllByCategory(category: Category): List<Transaction> {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val transactions = session.createQuery(
            """SELECT DISTINCT t FROM Transaction t
               LEFT JOIN FETCH t.party LEFT JOIN FETCH t.account
               LEFT JOIN FETCH t.category LEFT JOIN FETCH t.subcategory
               LEFT JOIN FETCH t.tags
               WHERE t.category = :category""",
            Transaction::class.java,
        ).setParameter("category", category).resultList
        session.transaction.commit()
        session.close()
        return transactions
    }

    override fun getAllBySubcategory(subcategory: Subcategory): List<Transaction> {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val transactions = session.createQuery(
            """SELECT DISTINCT t FROM Transaction t
               LEFT JOIN FETCH t.party LEFT JOIN FETCH t.account
               LEFT JOIN FETCH t.category LEFT JOIN FETCH t.subcategory
               LEFT JOIN FETCH t.tags
               WHERE t.subcategory = :subcategory""",
            Transaction::class.java,
        ).setParameter("subcategory", subcategory).resultList
        session.transaction.commit()
        session.close()
        return transactions
    }

    override fun findByScheduleId(scheduleId: Long): List<Transaction> {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val transactions = session.createQuery(
            """SELECT DISTINCT t FROM Transaction t
               LEFT JOIN FETCH t.party LEFT JOIN FETCH t.account
               LEFT JOIN FETCH t.category LEFT JOIN FETCH t.subcategory
               LEFT JOIN FETCH t.tags
               WHERE t.scheduleId = :scheduleId
               ORDER BY t.date ASC""",
            Transaction::class.java,
        ).setParameter("scheduleId", scheduleId).resultList
        session.transaction.commit()
        session.close()
        return transactions
    }

    override fun update(transaction: Transaction) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.merge(transaction)
        session.transaction.commit()
        session.close()
    }

    override fun insert(transaction: Transaction) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.merge(transaction)
        session.transaction.commit()
        session.close()
    }

    override fun delete(transaction: Transaction) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val managed = session.merge(transaction)
        managed.tags?.clear()
        session.remove(managed)
        session.transaction.commit()
        session.close()
    }
}
