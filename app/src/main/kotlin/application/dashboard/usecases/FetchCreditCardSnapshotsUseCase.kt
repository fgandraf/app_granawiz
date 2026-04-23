package application.dashboard.usecases

import domain.contracts.IGroupRepository
import domain.contracts.ITransactionRepository
import domain.entity.account.CreditCardAccount
import domain.enums.TransactionType
import domain.structs.CreditCardSnapshot
import infrastructure.repository.GroupRepository
import infrastructure.repository.TransactionRepository
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.temporal.ChronoUnit

class FetchCreditCardSnapshotsUseCase(
    private val groupRepository: IGroupRepository = GroupRepository(),
    private val transactionRepository: ITransactionRepository = TransactionRepository(),
) {

    fun execute(today: LocalDate = LocalDate.now()): List<CreditCardSnapshot> {
        val cards = groupRepository.getAll()
            .flatMap { it.accounts }
            .filterIsInstance<CreditCardAccount>()

        return cards.map { card -> snapshot(card, today) }
    }

    private fun snapshot(card: CreditCardAccount, today: LocalDate): CreditCardSnapshot {
        val (cycleStart, cycleEnd) = currentCycle(card, today)

        val invoice = transactionRepository
            .getByDateRange(
                from = cycleStart.atStartOfDay(),
                to = cycleEnd.atTime(LocalTime.MAX),
                type = TransactionType.EXPENSE,
            )
            .filter { it.account.id == card.id }
            .sumOf { kotlin.math.abs(it.balance) }

        val nextDue = nextDueDate(card, today)
        val daysToDue = ChronoUnit.DAYS.between(today, nextDue)

        return CreditCardSnapshot(
            account = card,
            currentInvoice = invoice,
            availableLimit = (card.creditLimit - invoice).coerceAtLeast(0.0),
            nextDueDate = nextDue,
            daysToDue = daysToDue,
        )
    }

    private fun currentCycle(card: CreditCardAccount, today: LocalDate): Pair<LocalDate, LocalDate> {
        val closingDay = card.closingDay.coerceIn(1, 28)
        val previousClose = if (today.dayOfMonth > closingDay) {
            safeDate(YearMonth.from(today), closingDay)
        } else {
            safeDate(YearMonth.from(today).minusMonths(1), closingDay)
        }
        val nextClose = previousClose.plusMonths(1)
        return previousClose.plusDays(1) to nextClose
    }

    private fun nextDueDate(card: CreditCardAccount, today: LocalDate): LocalDate {
        val dueDay = card.dueDay.coerceIn(1, 28)
        val thisMonth = safeDate(YearMonth.from(today), dueDay)
        return if (!thisMonth.isBefore(today)) thisMonth
        else safeDate(YearMonth.from(today).plusMonths(1), dueDay)
    }

    private fun safeDate(ym: YearMonth, day: Int): LocalDate =
        ym.atDay(day.coerceAtMost(ym.lengthOfMonth()))
}
