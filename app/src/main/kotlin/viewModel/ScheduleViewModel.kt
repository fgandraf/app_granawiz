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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _schedules = MutableStateFlow(emptyList<Schedule>())
    val schedules: StateFlow<List<Schedule>> = _schedules.asStateFlow()

    private val _paidTransactions = MutableStateFlow(emptyList<Transaction>())
    val paidTransactions: StateFlow<List<Transaction>> = _paidTransactions.asStateFlow()

    fun getSchedules() {
        runCatching {
            _schedules.value = scheduleHandler.fetchSchedules(account = selectedAccount)
            _paidTransactions.value = transactionHandler.fetchTransactions(account = selectedAccount)
                .filter { it.scheduleId != null && it.originalDueDate != null }
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    private val _groups = MutableStateFlow(emptyList<Group>())
    val groups: StateFlow<List<Group>> = _groups.asStateFlow()

    fun getGroups() {
        runCatching {
            _groups.value = groupHandler.fetchGroups()
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
            val paid = _paidTransactions.value
            _schedules.value.flatMap { schedule ->
                scheduleHandler.generateOccurrences(schedule, windowStart, windowEnd, paid)
            }.sortedBy { it.dueDate }
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
         .getOrDefault(emptyList())
    }
}
