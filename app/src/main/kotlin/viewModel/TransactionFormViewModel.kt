package viewModel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import core.entity.*
import core.entity.account.BankAccount
import core.enums.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import service.TransactionService
import view.theme.Lime800
import view.theme.Red800
import java.time.LocalDateTime
import kotlin.math.abs

class TransactionFormViewModel(transaction: Transaction? = null) {

    val service: TransactionService = TransactionService()

    var id by mutableStateOf(0L)
    var party = MutableStateFlow(Party())
    var account by mutableStateOf(BankAccount())
    var category by mutableStateOf(Category())
    var subCategory by mutableStateOf<Subcategory?>(null)
    var tags = MutableStateFlow(listOf<Tag>())
    var date by mutableStateOf(LocalDateTime.now())
    var description by mutableStateOf("")
    var balance by mutableStateOf(0.0)
    var type by mutableStateOf(TransactionType.NEUTRAL)

    init {
        transaction?.let {
            id = it.id
            party.value = it.party
            account = it.account
            category = it.category
            subCategory = it.subcategory
            tags.value = it.tags?: listOf()
            date = it.date
            description = it.description
            balance = it.balance
            type = it.type
        }
    }

    fun updateBalance(value: String = ""){
        balance = if(value != "")
            value.replace(".", "").replace(",", ".").toDouble()
        else balance

        balance = if (type == TransactionType.EXPENSE && balance != 0.0) -abs(balance) else abs(balance)
    }


    val typeColor = derivedStateOf {
        when (type) {
            TransactionType.EXPENSE -> Red800
            TransactionType.GAIN -> Lime800
            TransactionType.NEUTRAL -> Color.Gray
        }
    }

    val typeLabel = derivedStateOf {
        when (type) {
            TransactionType.EXPENSE -> "Despesa"
            TransactionType.GAIN -> "Receita"
            else -> ""
        }
    }

    fun saveTransaction(){
        val transaction = Transaction(
            id = this.id,
            party = party.value,
            account = account,
            category = category,
            subcategory = subCategory,
            tags = tags.value,
            date = date,
            description = description,
            balance = balance,
            type = type,
        )

        if (id == 0L) service.addTransaction(transaction)
        else service.updateTransaction(transaction)
    }


}