package domain.contracts

import domain.entity.Category
import domain.entity.Subcategory
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.enums.TransactionType
import java.time.LocalDateTime

interface ITransactionRepository {

    fun getAll(): List<Transaction>

    fun getAllByAccount(account: BankAccount): List<Transaction>

    fun getAllByCategory(category: Category): List<Transaction>

    fun getAllBySubcategory(subcategory: Subcategory): List<Transaction>

    fun getByDateRange(from: LocalDateTime, to: LocalDateTime, type: TransactionType? = null): List<Transaction>

    fun findByScheduleId(scheduleId: Long): List<Transaction>

    fun insert(transaction: Transaction)

    fun update(transaction: Transaction)

    fun delete(transaction: Transaction)

}
