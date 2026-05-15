package viewModel

import domain.entity.Group
import domain.entity.Schedule
import domain.entity.Transaction
import domain.entity.account.BankAccount
import application.account.AccountHandler
import infrastructure.di.ApplicationContainer
import application.group.GroupHandler
import application.schedule.ScheduleHandler
import application.schedule.usecases.ScheduleOccurrence
import application.transaction.TransactionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import viewModel.shared.AppEvents
import viewModel.shared.UiEvent
import java.io.File
import java.time.LocalDateTime

class ScheduleViewModel(
    account: BankAccount? = null,
    private val scheduleHandler: ScheduleHandler = ApplicationContainer.scheduleHandler,
    private val transactionHandler: TransactionHandler = ApplicationContainer.transactionHandler,
    private val groupHandler: GroupHandler = ApplicationContainer.groupHandler,
    private val accountHandler: AccountHandler = ApplicationContainer.accountHandler,
) {

    var selectedAccount = account

    var schedules = MutableStateFlow(emptyList<Schedule>())
    var paidTransactions = MutableStateFlow(emptyList<Transaction>())

    fun getSchedules() {
        runCatching {
            schedules.value = scheduleHandler.fetchSchedules(account = selectedAccount)
            paidTransactions.value = transactionHandler.fetchTransactions(account = selectedAccount)
                .filter { it.scheduleId != null && it.originalDueDate != null }
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    var groups = MutableStateFlow(emptyList<Group>())
    fun getGroups() {
        runCatching {
            groups.value = groupHandler.fetchGroups()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    init {
        getSchedules()
        if (account == null) getGroups()
    }

    fun deleteThisOccurrence(occurrence: ScheduleOccurrence) {
        runCatching {
            scheduleHandler.deleteThisOccurrence(occurrence)
            getSchedules()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun deleteThisAndFuture(occurrence: ScheduleOccurrence) {
        runCatching {
            scheduleHandler.deleteThisAndFuture(occurrence)
            getSchedules()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun markAsPaid(occurrence: ScheduleOccurrence) {
        runCatching {
            scheduleHandler.markAsPaid(occurrence.schedule, occurrence.dueDate, occurrence.index)
            val accountTransactions = transactionHandler.fetchTransactions(account = occurrence.schedule.account)
            accountHandler.updateBalance(occurrence.schedule.account.id, accountTransactions.sumOf { it.balance })
            getSchedules()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun exportToExcel(occurrences: List<ScheduleOccurrence>, file: File) {
        runCatching {
            scheduleHandler.exportSchedulesToExcel(occurrences, file)
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun buildOccurrences(
        windowStart: LocalDateTime,
        windowEnd: LocalDateTime,
    ): List<ScheduleOccurrence> {
        return runCatching {
            val paid = paidTransactions.value
            schedules.value.flatMap { schedule ->
                scheduleHandler.generateOccurrences(schedule, windowStart, windowEnd, paid)
            }.sortedBy { it.dueDate }
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
         .getOrDefault(emptyList())
    }
}
