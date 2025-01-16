package viewModel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import core.entity.*
import core.entity.account.BankAccount
import core.enums.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import domain.transaction.TransactionHandler
import java.time.LocalDateTime
import kotlin.math.abs

class TransactionFormViewModel(private val usecases: TransactionHandler = TransactionHandler()) {

    var id by mutableStateOf(0L)
    var party : MutableStateFlow<Party?> = MutableStateFlow(null)
    var account by mutableStateOf(BankAccount())
    var category : MutableStateFlow<Category?> = MutableStateFlow(null)
    var subCategory by mutableStateOf<Subcategory?>(null)
    var tags = MutableStateFlow(listOf<Tag>())
    var date by mutableStateOf(LocalDateTime.now())
    var description by mutableStateOf("")
    var balance by mutableStateOf(0.0)
    var type by mutableStateOf(TransactionType.NEUTRAL)

    fun loadFromTransaction(transaction: Transaction) {
        transaction.let {
            id = it.id
            party.value = it.party
            account = it.account
            category.value = if(it.category.id != 0L) it.category else null
            subCategory = it.subcategory
            tags.value = it.tags?: listOf()
            date = it.date
            description = it.description
            balance = it.balance
            type = it.type
        }
    }

    fun clear(){
        id = 0L
        party.value = null
        account = BankAccount()
        category.value = null
        subCategory = null
        tags.value = listOf()
        date = LocalDateTime.now()
        description = ""
        balance = 0.0
        type = TransactionType.NEUTRAL
    }

    fun updateBalance(value: String = ""){
        balance = if(value != "")
            value.replace(".", "").replace(",", ".").toDouble()
        else balance

        balance = if (type == TransactionType.EXPENSE && balance != 0.0) -abs(balance) else abs(balance)
    }


    val typeLabel = derivedStateOf {
        when (type) {
            TransactionType.EXPENSE -> "Despesa"
            TransactionType.GAIN -> "Receita"
            else -> ""
        }
    }


    fun saveTransaction(){
        usecases.saveTransaction(this)
    }


}