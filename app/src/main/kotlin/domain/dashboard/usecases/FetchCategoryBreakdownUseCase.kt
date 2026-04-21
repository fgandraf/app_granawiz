package domain.dashboard.usecases

import core.enums.TransactionType
import core.structs.CategoryBreakdown
import infra.dao.TransactionDao
import java.time.LocalDateTime

class FetchCategoryBreakdownUseCase(private val transactionDao: TransactionDao = TransactionDao()) {

    fun execute(
        from: LocalDateTime,
        to: LocalDateTime,
        type: TransactionType = TransactionType.EXPENSE,
        limit: Int = 6,
    ): List<CategoryBreakdown> {

        val transactions = transactionDao.getByDateRange(from, to, type)
        if (transactions.isEmpty()) return emptyList()

        val total = transactions.sumOf { kotlin.math.abs(it.balance) }.takeIf { it > 0.0 } ?: return emptyList()

        val grouped = transactions.groupBy { it.category }
            .map { (category, txs) ->
                val amount = txs.sumOf { kotlin.math.abs(it.balance) }
                CategoryBreakdown(
                    category = category,
                    amount = amount,
                    percent = amount / total * 100.0,
                    transactionCount = txs.size,
                )
            }
            .sortedByDescending { it.amount }

        if (grouped.size <= limit) return grouped

        val top = grouped.take(limit - 1)
        val rest = grouped.drop(limit - 1)
        val restAmount = rest.sumOf { it.amount }
        val restCount = rest.sumOf { it.transactionCount }

        val othersCategory = core.entity.Category(
            id = -1L,
            type = rest.first().category.type,
            name = "Outros",
            icon = "_default.svg",
            subcategories = mutableListOf(),
        )

        return top + CategoryBreakdown(
            category = othersCategory,
            amount = restAmount,
            percent = restAmount / total * 100.0,
            transactionCount = restCount,
        )
    }
}
