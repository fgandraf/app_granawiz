package viewModel

import domain.entity.Group
import domain.entity.Schedule
import domain.entity.Transaction
import domain.entity.account.BankAccount
import application.group.GroupHandler
import application.schedule.ScheduleHandler
import application.schedule.usecases.ScheduleOccurrence
import application.transaction.TransactionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime

class ScheduleViewModel(
    account: BankAccount? = null,
    private val scheduleHandler: ScheduleHandler = ScheduleHandler(),
    private val transactionHandler: TransactionHandler = TransactionHandler(),
    private val groupHandler: GroupHandler = GroupHandler(),
) {

    var selectedAccount = account

    var schedules = MutableStateFlow(emptyList<Schedule>())
    var paidTransactions = MutableStateFlow(emptyList<Transaction>())

    fun getSchedules() {
        schedules.value = scheduleHandler.fetchSchedules(account = selectedAccount)
        paidTransactions.value = transactionHandler.fetchTransactions(account = selectedAccount)
            .filter { it.scheduleId != null && it.originalDueDate != null }
    }

    var groups = MutableStateFlow(emptyList<Group>())
    fun getGroups() {
        groups.value = groupHandler.fetchGroups()
    }

    init {
        getSchedules()
        if (account == null) getGroups()
    }

    fun deleteThisOccurrence(occurrence: ScheduleOccurrence) {
        scheduleHandler.deleteThisOccurrence(occurrence)
        getSchedules()
    }

    fun deleteThisAndFuture(occurrence: ScheduleOccurrence) {
        scheduleHandler.deleteThisAndFuture(occurrence)
        getSchedules()
    }

    fun markAsPaid(occurrence: ScheduleOccurrence) {
        scheduleHandler.markAsPaid(occurrence.schedule, occurrence.dueDate)
        getSchedules()
    }

    fun buildOccurrences(
        windowStart: LocalDateTime,
        windowEnd: LocalDateTime,
    ): List<ScheduleOccurrence> {
        val paid = paidTransactions.value
        return schedules.value.flatMap { schedule ->
            scheduleHandler.generateOccurrences(schedule, windowStart, windowEnd, paid)
        }.sortedBy { it.dueDate }
    }

}
