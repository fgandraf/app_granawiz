package domain.structs

import domain.entity.Category
import domain.entity.Party
import domain.entity.Transaction
import domain.entity.account.CreditCardAccount
import application.schedule.usecases.ScheduleOccurrence
import java.time.LocalDate

data class DashboardSummary(
    val period: DashboardPeriod,
    val netWorth: NetWorthSnapshot,
    val cashFlow: CashFlow,
    val spendingPace: SpendingPace?,
    val creditCards: List<CreditCardSnapshot>,
    val categoryBreakdown: List<CategoryBreakdown>,
    val monthlyEvolution: List<MonthlyFlow>,
    val topParties: List<PartyVolume>,
    val topTransactions: List<Transaction>,
    val savingsRatePercent: Double,
    val upcomingOccurrences: List<ScheduleOccurrence>,
)

data class NetWorthSnapshot(
    val total: Double,
    val deltaAmount: Double,
    val deltaPercent: Double,
)

data class CashFlow(
    val income: Double,
    val expense: Double,
) {
    val net: Double get() = income - expense
}

data class SpendingPace(
    val today: LocalDate,
    val monthTotalSoFar: Double,
    val averageAtSameDay: Double,
    val percentOfAverage: Double,
)

data class CreditCardSnapshot(
    val account: CreditCardAccount,
    val currentInvoice: Double,
    val availableLimit: Double,
    val nextDueDate: LocalDate,
    val daysToDue: Long,
)

data class CategoryBreakdown(
    val category: Category,
    val amount: Double,
    val percent: Double,
    val transactionCount: Int,
)

data class MonthlyFlow(
    val label: String,
    val income: Double,
    val expense: Double,
) {
    val net: Double get() = income - expense
}

data class PartyVolume(
    val party: Party,
    val amount: Double,
    val transactionCount: Int,
)
