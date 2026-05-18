package application.dashboard.usecases

import domain.contracts.ITransactionRepository
import domain.entity.Category
import domain.entity.Party
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.enums.PartyType
import domain.enums.TransactionType
import domain.structs.DashboardPeriod
import domain.structs.NetWorthSnapshot
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class BuildDashboardSummaryUseCaseTest {

    private val txRepo = mockk<ITransactionRepository>()
    private val netWorthUc = mockk<FetchNetWorthDeltaUseCase>()
    private val monthlyFlowUc = mockk<FetchMonthlyFlowUseCase>()
    private val categoryUc = mockk<FetchCategoryBreakdownUseCase>()
    private val creditCardsUc = mockk<FetchCreditCardSnapshotsUseCase>()
    private val topPartiesUc = mockk<FetchTopPartiesUseCase>()
    private val topTxUc = mockk<FetchTopTransactionsUseCase>()
    private val spendingPaceUc = mockk<FetchSpendingPaceUseCase>()
    private val upcomingUc = mockk<FetchUpcomingOccurrencesUseCase>()

    private val useCase = BuildDashboardSummaryUseCase(
        txRepo, netWorthUc, monthlyFlowUc, categoryUc,
        creditCardsUc, topPartiesUc, topTxUc, spendingPaceUc, upcomingUc,
    )

    private val today = LocalDate.of(2024, 3, 15)
    private val party = Party(id = 1L, name = "P", type = PartyType.PAYER)

    private fun tx(balance: Double, type: TransactionType, date: LocalDateTime = LocalDateTime.of(2024, 3, 10, 10, 0)) =
        Transaction(
            party = party, account = BankAccount(), category = Category(),
            subcategory = null, tags = null,
            date = date, description = "T", balance = balance, type = type,
        )

    private fun setupDefaults() {
        every { netWorthUc.execute(today) } returns NetWorthSnapshot(1000.0, 100.0, 10.0)
        every { monthlyFlowUc.execute(any(), any(), any(), any()) } returns emptyList()
        every { categoryUc.execute(any(), any(), any(), any()) } returns emptyList()
        every { creditCardsUc.execute(today) } returns emptyList()
        every { topPartiesUc.execute(any(), any(), any(), any()) } returns emptyList()
        every { topTxUc.execute(any(), any(), any(), any()) } returns emptyList()
        every { spendingPaceUc.execute(today, any()) } returns null
        every { upcomingUc.execute(today, any()) } returns emptyList()
    }

    @Test
    fun `cash flow income and expense are correctly computed from period transactions`() {
        setupDefaults()
        val (from, to) = DashboardPeriod.ThisMonth.range(today)
        every { txRepo.getByDateRange(from, to) } returns listOf(
            tx(500.0, TransactionType.GAIN),
            tx(-200.0, TransactionType.EXPENSE),
            tx(-50.0, TransactionType.EXPENSE),
        )

        val summary = useCase.execute(DashboardPeriod.ThisMonth, today)

        assertEquals(500.0, summary.cashFlow.income, 0.01)
        assertEquals(250.0, summary.cashFlow.expense, 0.01)
    }

    @Test
    fun `savings rate is income minus expense over income`() {
        setupDefaults()
        val (from, to) = DashboardPeriod.ThisMonth.range(today)
        every { txRepo.getByDateRange(from, to) } returns listOf(
            tx(1000.0, TransactionType.GAIN),
            tx(-400.0, TransactionType.EXPENSE),
        )

        val summary = useCase.execute(DashboardPeriod.ThisMonth, today)

        assertEquals(60.0, summary.savingsRatePercent, 0.01)
    }

    @Test
    fun `savings rate is zero when no income`() {
        setupDefaults()
        val (from, to) = DashboardPeriod.ThisMonth.range(today)
        every { txRepo.getByDateRange(from, to) } returns listOf(
            tx(-300.0, TransactionType.EXPENSE),
        )

        val summary = useCase.execute(DashboardPeriod.ThisMonth, today)

        assertEquals(0.0, summary.savingsRatePercent, 0.01)
    }

    @Test
    fun `spending pace is included only for ThisMonth period`() {
        setupDefaults()
        val (from, to) = DashboardPeriod.ThisMonth.range(today)
        every { txRepo.getByDateRange(from, to) } returns emptyList()

        useCase.execute(DashboardPeriod.ThisMonth, today)

        verify { spendingPaceUc.execute(today, any()) }
    }

    @Test
    fun `spending pace is null for non-ThisMonth periods`() {
        setupDefaults()
        val (from, to) = DashboardPeriod.LastMonth.range(today)
        every { txRepo.getByDateRange(from, to) } returns emptyList()

        val summary = useCase.execute(DashboardPeriod.LastMonth, today)

        assertNull(summary.spendingPace)
    }

    @Test
    fun `Last3Years period uses YEAR granularity for monthly evolution`() {
        setupDefaults()
        val (from, to) = DashboardPeriod.Last3Years.range(today)
        every { txRepo.getByDateRange(from, to) } returns emptyList()

        useCase.execute(DashboardPeriod.Last3Years, today)

        verify { monthlyFlowUc.execute(any(), any(), FetchMonthlyFlowUseCase.Granularity.YEAR, any()) }
    }
}
