package viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import core.entity.Group
import core.entity.account.BankAccount
import core.entity.account.CheckingAccount
import core.entity.account.CreditCardAccount
import core.entity.account.SavingsAccount
import core.enums.AccountType
import service.AccountService

class AccountFormViewModel {

    val service = AccountService(viewModel = this)

    var name by mutableStateOf("")
    var icon by mutableStateOf("_default.svg")
    var openBalance by mutableStateOf(0.0)
    var limit by mutableStateOf(0.0)
    var group by mutableStateOf(Group())
    var description by mutableStateOf("")
    var closingDay by mutableStateOf(0)
    var dueDay by mutableStateOf(0)


    fun initializeFromAccount(account: BankAccount){
        account.let {
            icon = it.icon
            name = it.name
            group = it.group
            description = it.description
        }
        when (account.type){
            AccountType.CHECKING -> {
                (account as CheckingAccount).let {
                   openBalance = it.balance
                   limit = it.overdraftLimit
               }
            }
            AccountType.SAVINGS -> {
                (account as SavingsAccount).let {
                    openBalance = it.balance
                }
            }
            AccountType.CREDIT_CARD -> {
                (account as CreditCardAccount).let {
                    limit = it.creditLimit
                    closingDay = it.closingDay
                    dueDay = it.dueDay
                }
            }
        }
    }
}