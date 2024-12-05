package viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import core.entity.*
import core.entity.account.BankAccount
import core.enums.TransactionType
import java.time.LocalDateTime
import kotlin.math.abs

class AddTransactionViewModel {

    var id by mutableStateOf(0L)
    var party by mutableStateOf(Party())
    var account by mutableStateOf(BankAccount())
    var category by mutableStateOf(Category())
    var subCategory by mutableStateOf<Subcategory?>(null)
    var tags: MutableSet<Tag>? = mutableSetOf()
    var date by mutableStateOf(LocalDateTime.now())
    var description by mutableStateOf("")
    var balance by mutableStateOf(0.0)
    var type by mutableStateOf<TransactionType>(TransactionType.NEUTRAL)

    fun initializeFromTransaction(transaction: Transaction){
        transaction.let {
            id = it.id
            party = it.party
            account = it.account
            category = it.category
            subCategory = it.subcategory ?: Subcategory()
            tags = it.tags
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
}