package infrastructure.repository

import domain.contracts.ITransactionRepository
import domain.entity.Category
import domain.entity.Subcategory
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.enums.TransactionType
import infrastructure.config.HibernateUtil
import jakarta.persistence.criteria.Predicate
import org.hibernate.Hibernate
import java.time.LocalDateTime

class TransactionRepository : ITransactionRepository {

    private val sessionFactory = HibernateUtil.getSessionFactory()

    override fun getAll(): List<Transaction> {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val criteriaBuilder = session.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(Transaction::class.java)
        val root = criteriaQuery.from(Transaction::class.java)
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get<LocalDateTime>("date")))
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

    override fun getByDateRange(
        from: LocalDateTime,
        to: LocalDateTime,
        type: TransactionType?,
    ): List<Transaction> {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val cb = session.criteriaBuilder
        val cq = cb.createQuery(Transaction::class.java)
        val root = cq.from(Transaction::class.java)

        val predicates = mutableListOf<Predicate>(
            cb.between(root.get("date"), from, to)
        )
        if (type != null) predicates.add(cb.equal(root.get<TransactionType>("type"), type))

        cq.where(*predicates.toTypedArray())
        cq.orderBy(cb.desc(root.get<LocalDateTime>("date")))

        val transactions = session.createQuery(cq).resultList
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

    override fun getAllByAccount(account: BankAccount): List<Transaction> {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val criteriaBuilder = session.criteriaBuilder

        val criteriaQuery = criteriaBuilder.createQuery(Transaction::class.java)

        val root = criteriaQuery.from(Transaction::class.java)
        criteriaQuery.where(criteriaBuilder.equal(root.get<BankAccount>("account"), account))
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get<LocalDateTime>("date")))

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

    override fun getAllByCategory(category: Category): List<Transaction> {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val cb = session.criteriaBuilder
        val cq = cb.createQuery(Transaction::class.java)
        val root = cq.from(Transaction::class.java)
        cq.where(cb.equal(root.get<Category>("category"), category))
        val transactions = session.createQuery(cq).resultList
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

    override fun getAllBySubcategory(subcategory: Subcategory): List<Transaction> {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val cb = session.criteriaBuilder
        val cq = cb.createQuery(Transaction::class.java)
        val root = cq.from(Transaction::class.java)
        cq.where(cb.equal(root.get<Subcategory>("subcategory"), subcategory))
        val transactions = session.createQuery(cq).resultList
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
        session.remove(transaction)
        session.transaction.commit()
        session.close()
    }


}