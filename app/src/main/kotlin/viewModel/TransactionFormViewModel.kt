package viewModel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import core.entity.*
import core.entity.account.BankAccount
import core.enums.TransactionType
import service.CategoryService
import view.theme.Lime800
import view.theme.Red800
import java.time.LocalDateTime
import kotlin.math.abs

class TransactionFormViewModel {

    private val categoryService: CategoryService = CategoryService()

    var id by mutableStateOf(0L)
    var party by mutableStateOf(Party())
    var account by mutableStateOf(BankAccount())
    var category by mutableStateOf(Category())
    var subCategory by mutableStateOf<Subcategory?>(null)
    var tags by mutableStateOf<MutableSet<Tag>?>(mutableSetOf())
    var date by mutableStateOf(LocalDateTime.now())
    var description by mutableStateOf("")
    var balance by mutableStateOf(0.0)
    var type by mutableStateOf(TransactionType.NEUTRAL)

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

    val categories = derivedStateOf { categoryService.categories }

    fun selectCategory(category: Category){
        this.category = category
    }
}