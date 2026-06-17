package viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import application.schedule.ScheduleHandler
import infrastructure.di.ApplicationContainer
import application.transaction.TransactionHandler
import domain.entity.*
import domain.entity.account.BankAccount
import domain.enums.ScheduleFrequency
import domain.enums.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime
import kotlin.math.abs

class TransactionFormViewModel(
    private val scheduleHandler: ScheduleHandler = ApplicationContainer.scheduleHandler,
    private val transactionHandler: TransactionHandler = ApplicationContainer.transactionHandler,
) {

    var id by mutableStateOf(0L)
    var transactionId by mutableStateOf(0L)

    private val _party = MutableStateFlow<Party?>(null)
    val party: StateFlow<Party?> = _party.asStateFlow()
    fun setParty(party: Party?) { _party.value = party }

    var account by mutableStateOf(BankAccount())
    var destinationAccount by mutableStateOf(BankAccount())
    var isTransfer by mutableStateOf(false)

    private val _category = MutableStateFlow<Category?>(null)
    val category: StateFlow<Category?> = _category.asStateFlow()
    fun setCategory(category: Category?) { _category.value = category }

    var subCategory by mutableStateOf<Subcategory?>(null)

    private val _tags = MutableStateFlow(listOf<Tag>())
    val tags: StateFlow<List<Tag>> = _tags.asStateFlow()
    fun setTags(tags: List<Tag>) { _tags.value = tags }

    var startDate by mutableStateOf(LocalDateTime.now())
    var description by mutableStateOf("")
    var balance by mutableStateOf(0.0)
    var type by mutableStateOf(TransactionType.NEUTRAL)
    var scheduleId by mutableStateOf<Long?>(null)
    var originalDueDate by mutableStateOf<LocalDateTime?>(null)
    var installment by mutableStateOf("1/1")

    var frequency by mutableStateOf(ScheduleFrequency.ONCE)
    var interval by mutableStateOf(1)
    var dayOfMonth by mutableStateOf<Int?>(null)
    var endDate by mutableStateOf<LocalDateTime?>(null)
    var installments by mutableStateOf<Int?>(null)

    fun loadFromSchedule(schedule: Schedule) {
        id = schedule.id
        transactionId = 0L
        _party.value = schedule.party
        account = schedule.account
        _category.value = if (schedule.category.id != 0L) schedule.category else null
        subCategory = schedule.subcategory
        _tags.value = schedule.tags ?: listOf()
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
        _party.value = transaction.party
        account = transaction.account
        _category.value = if (transaction.category.id != 0L) transaction.category else null
        subCategory = transaction.subcategory
        _tags.value = transaction.tags ?: listOf()
        startDate = transaction.date
        description = transaction.description
        balance = transaction.balance
        type = transaction.type
        scheduleId = transaction.scheduleId
        originalDueDate = transaction.originalDueDate
        installment = transaction.installment
        frequency = ScheduleFrequency.ONCE
        interval = 1
        dayOfMonth = null
        endDate = null
        installments = null
    }

    fun clear() {
        id = 0L
        transactionId = 0L
        _party.value = null
        account = BankAccount()
        destinationAccount = BankAccount()
        isTransfer = false
        _category.value = null
        subCategory = null
        _tags.value = listOf()
        startDate = LocalDateTime.now()
        description = ""
        balance = 0.0
        type = TransactionType.NEUTRAL
        scheduleId = null
        originalDueDate = null
        installment = "1/1"
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

    fun save() {
        if (isTransfer) {
            transactionHandler.saveTransfer(
                source = account,
                destination = destinationAccount,
                amount = abs(balance),
                date = startDate,
                description = description,
            )
        } else if (transactionId != 0L) {
            transactionHandler.saveTransaction(
                Transaction(
                    id = transactionId,
                    party = _party.value!!,
                    account = account,
                    category = _category.value!!,
                    subcategory = subCategory,
                    tags = _tags.value.toMutableList(),
                    date = startDate,
                    description = description,
                    balance = balance,
                    type = type,
                    scheduleId = scheduleId,
                    originalDueDate = originalDueDate,
                    installment = installment,
                )
            )
        } else {
            scheduleHandler.saveSchedule(
                Schedule(
                    id = id,
                    party = _party.value!!,
                    account = account,
                    category = _category.value!!,
                    subcategory = subCategory,
                    tags = _tags.value.toMutableList(),
                    startDate = startDate,
                    description = description,
                    balance = balance,
                    type = type,
                    frequency = frequency,
                    interval = interval,
                    dayOfMonth = dayOfMonth,
                    endDate = endDate,
                    installments = installments,
                )
            )
        }
    }

}
