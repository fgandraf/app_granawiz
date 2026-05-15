package application.dashboard.usecases

import domain.contracts.ITransactionRepository
import domain.enums.TransactionType
import domain.structs.CashFlow
import domain.structs.DashboardPeriod
import domain.structs.DashboardSummary
import java.time.LocalDate

class BuildDashboardSummaryUseCase(
    private val transactionRepository: ITransactionRepository,
    private val netWorthUseCase: FetchNetWorthDeltaUseCase,
    private val monthlyFlowUseCase: FetchMonthlyFlowUseCase,
    private val categoryBreakdownUseCase: FetchCategoryBreakdownUseCase,
    private val creditCardsUseCase: FetchCreditCardSnapshotsUseCase,
    private val topPartiesUseCase: FetchTopPartiesUseCase,
    private val topTransactionsUseCase: FetchTopTransactionsUseCase,
    private val spendingPaceUseCase: FetchSpendingPaceUseCase,
    private val upcomingOccurrencesUseCase: FetchUpcomingOccurrencesUseCase,
) {

    fun execute(period: DashboardPeriod, today: LocalDate = LocalDate.now()): DashboardSummary {
        val (from, to) = period.range(today)

        val periodTxs = transactionRepository.getByDateRange(from, to)
        val income = periodTxs.filter { it.type == TransactionType.GAIN }.sumOf { kotlin.math.abs(it.balance) }
        val expense = periodTxs.filter { it.type == TransactionType.EXPENSE }.sumOf { kotlin.math.abs(it.balance) }
        val cashFlow = CashFlow(income, expense)
        val savingsRate = if (income > 0.0) (income - expense) / income * 100.0 else 0.0

        return DashboardSummary(
            period = period,
            netWorth = netWorthUseCase.execute(today),
            cashFlow = cashFlow,
            spendingPace = if (period == DashboardPeriod.ThisMonth) spendingPaceUseCase.execute(today) else null,
            creditCards = creditCardsUseCase.execute(today),
            categoryBreakdown = categoryBreakdownUseCase.execute(from, to),
            monthlyEvolution = monthlyFlowUseCase.execute(
                from = from,
                to = to,
                granularity = if (period == DashboardPeriod.Last3Years) {
                    FetchMonthlyFlowUseCase.Granularity.YEAR
                } else {
                    FetchMonthlyFlowUseCase.Granularity.MONTH
                },
            ),
            topParties = topPartiesUseCase.execute(from, to),
            topTransactions = topTransactionsUseCase.execute(from, to),
            savingsRatePercent = savingsRate,
            upcomingOccurrences = upcomingOccurrencesUseCase.execute(today),
        )
    }
}
