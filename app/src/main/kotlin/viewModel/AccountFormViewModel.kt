package viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import domain.entity.Group
import domain.entity.account.BankAccount
import domain.entity.account.CheckingAccount
import domain.entity.account.CreditCardAccount
import domain.entity.account.SavingsAccount
import domain.enums.AccountType
import application.account.AccountHandler

class AccountFormViewModel(private val accountHandler: AccountHandler = AccountHandler()) {

    var id by mutableStateOf(0L)
    var type by mutableStateOf(AccountType.CHECKING)
    var name by mutableStateOf("")
    var icon by mutableStateOf("_default.svg")
    var balance by mutableStateOf(0.0)
    var limit by mutableStateOf(0.0)
    var group by mutableStateOf(Group())
    var description by mutableStateOf("")
    var position by mutableStateOf(0)
    var closingDay by mutableStateOf(0)
    var dueDay by mutableStateOf(0)


    fun initializeFromAccount(account: BankAccount) {
        account.let {
            id = it.id
            type = it.type
            icon = it.icon
            name = it.name
            group = it.group
            description = it.description
            position = it.position
        }
        when (account.type) {
            AccountType.CHECKING -> {
                (account as CheckingAccount).let {
                    balance = it.balance
                    limit = it.overdraftLimit
                }
            }

            AccountType.SAVINGS -> {
                (account as SavingsAccount).let {
                    balance = it.balance
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

    fun saveAccount() {
        val account = accountHandler.buildAccount(this)
        accountHandler.saveAccount(account)
    }
}