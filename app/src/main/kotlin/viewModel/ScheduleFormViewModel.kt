package viewModel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import core.entity.*
import core.entity.account.BankAccount
import core.enums.ScheduleFrequency
import core.enums.TransactionType
import domain.schedule.ScheduleHandler
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime
import kotlin.math.abs

class ScheduleFormViewModel(private val scheduleHandler: ScheduleHandler = ScheduleHandler()) {

    var id by mutableStateOf(0L)
    var party: MutableStateFlow<Party?> = MutableStateFlow(null)
    var account by mutableStateOf(BankAccount())
    var category: MutableStateFlow<Category?> = MutableStateFlow(null)
    var subCategory by mutableStateOf<Subcategory?>(null)
    var tags = MutableStateFlow(listOf<Tag>())
    var startDate by mutableStateOf(LocalDateTime.now())
    var description by mutableStateOf("")
    var balance by mutableStateOf(0.0)
    var type by mutableStateOf(TransactionType.NEUTRAL)

    var frequency by mutableStateOf(ScheduleFrequency.MONTHLY)
    var interval by mutableStateOf(1)
    var dayOfMonth by mutableStateOf<Int?>(null)
    var endDate by mutableStateOf<LocalDateTime?>(null)
    var installments by mutableStateOf<Int?>(null)

    fun loadFromSchedule(schedule: Schedule) {
        schedule.let {
            id = it.id
            party.value = it.party
            account = it.account
            category.value = if (it.category.id != 0L) it.category else null
            subCategory = it.subcategory
            tags.value = it.tags ?: listOf()
            startDate = it.startDate
            description = it.description
            balance = it.balance
            type = it.type
            frequency = it.frequency
            interval = it.interval
            dayOfMonth = it.dayOfMonth
            endDate = it.endDate
            installments = it.installments
        }
    }

    fun clear() {
        id = 0L
        party.value = null
        account = BankAccount()
        category.value = null
        subCategory = null
        tags.value = listOf()
        startDate = LocalDateTime.now()
        description = ""
        balance = 0.0
        type = TransactionType.NEUTRAL
        frequency = ScheduleFrequency.MONTHLY
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
            TransactionType.EXPENSE -> "Despesa agendada"
            TransactionType.GAIN -> "Receita agendada"
            else -> ""
        }
    }

    fun saveSchedule() {
        scheduleHandler.saveSchedule(this)
    }

}
