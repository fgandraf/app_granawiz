package viewModel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import domain.entity.*
import domain.entity.account.BankAccount
import domain.enums.ScheduleFrequency
import domain.enums.TransactionType
import application.schedule.ScheduleHandler
import application.transaction.TransactionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime
import kotlin.math.abs

class TransactionFormViewModel(
    private val scheduleHandler: ScheduleHandler = ScheduleHandler(),
    private val transactionHandler: TransactionHandler = TransactionHandler(),
) {

    var id by mutableStateOf(0L)
    var transactionId by mutableStateOf(0L)
    var party: MutableStateFlow<Party?> = MutableStateFlow(null)
    var account by mutableStateOf(BankAccount())
    var category: MutableStateFlow<Category?> = MutableStateFlow(null)
    var subCategory by mutableStateOf<Subcategory?>(null)
    var tags = MutableStateFlow(listOf<Tag>())
    var startDate by mutableStateOf(LocalDateTime.now())
    var description by mutableStateOf("")
    var balance by mutableStateOf(0.0)
    var type by mutableStateOf(TransactionType.NEUTRAL)
    var scheduleId by mutableStateOf<Long?>(null)
    var originalDueDate by mutableStateOf<LocalDateTime?>(null)

    var frequency by mutableStateOf(ScheduleFrequency.ONCE)
    var interval by mutableStateOf(1)
    var dayOfMonth by mutableStateOf<Int?>(null)
    var endDate by mutableStateOf<LocalDateTime?>(null)
    var installments by mutableStateOf<Int?>(null)

    fun loadFromSchedule(schedule: Schedule) {
        id = schedule.id
        transactionId = 0L
        party.value = schedule.party
        account = schedule.account
        category.value = if (schedule.category.id != 0L) schedule.category else null
        subCategory = schedule.subcategory
        tags.value = schedule.tags ?: listOf()
        startDate = schedule.startDate
        description = schedule.description
        balance = schedule.balance
        type = schedule.type
        frequency = schedule.frequency
        interval = schedule.interval
        dayOfMonth = schedule.dayOfMonth
        endDate = schedule.endDate
        installments = schedule.installments
        scheduleId = null
        originalDueDate = null
    }

    fun loadFromTransaction(transaction: Transaction) {
        transactionId = transaction.id
        id = 0L
        party.value = transaction.party
        account = transaction.account
        category.value = if (transaction.category.id != 0L) transaction.category else null
        subCategory = transaction.subcategory
        tags.value = transaction.tags ?: listOf()
        startDate = transaction.date
        description = transaction.description
        balance = transaction.balance
        type = transaction.type
        scheduleId = transaction.scheduleId
        originalDueDate = transaction.originalDueDate
        frequency = ScheduleFrequency.ONCE
        interval = 1
        dayOfMonth = null
        endDate = null
        installments = null
    }

    fun clear() {
        id = 0L
        transactionId = 0L
        party.value = null
        account = BankAccount()
        category.value = null
        subCategory = null
        tags.value = listOf()
        startDate = LocalDateTime.now()
        description = ""
        balance = 0.0
        type = TransactionType.NEUTRAL
        scheduleId = null
        originalDueDate = null
        frequency = ScheduleFrequency.ONCE
        interval = 1
        dayOfMonth = null
        endDate = null
        installments = null
    }

    fun updateBalance(value: String = "") {
        balance = if (value != "")
            value.replace(".", "").replace(",", ".").toDouble()
        else balance

        balance = if (type == TransactionType.EXPENSE && balance != 0.0) -abs(balance) else abs(balance)
    }

    val typeLabel = derivedStateOf {
        when (type) {
            TransactionType.EXPENSE -> if (transactionId != 0L) "Despesa" else "Despesa agendada"
            TransactionType.GAIN -> if (transactionId != 0L) "Receita" else "Receita agendada"
            else -> ""
        }
    }

    fun save() {
        if (transactionId != 0L) {
            transactionHandler.saveTransaction(
                Transaction(
                    id = transactionId,
                    party = party.value!!,
                    account = account,
                    category = category.value!!,
                    subcategory = subCategory,
                    tags = tags.value,
                    date = startDate,
                    description = description,
                    balance = balance,
                    type = type,
                    scheduleId = scheduleId,
                    originalDueDate = originalDueDate,
                )
            )
        } else {
            scheduleHandler.saveSchedule(this)
        }
    }

}
