package viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import model.dao.AccountDao
import model.entity.Group
import model.entity.account.CheckingAccount
import model.entity.account.CreditCardAccount
import model.entity.account.SavingsAccount

class AddAccountViewModel {

    var name by mutableStateOf("")
    var icon by mutableStateOf("_default.svg")
    var openBalance by mutableStateOf(0.0)
    var limit by mutableStateOf(0.0)
    var group by mutableStateOf(Group())
    var description by mutableStateOf("")
    var closingDay by mutableStateOf(0)
    var dueDay by mutableStateOf(0)


    fun addCheckingAccount(groups: List<Group>) {
        val checkingAccount = CheckingAccount(
            name = this.name,
            description = this.description,
            position = groups.find { it.id == this.group.id }!!.accounts.size + 1,
            icon = this.icon,
            balance = this.openBalance,
            group = this.group,
            openBalance = this.openBalance,
            overdraftLimit = this.limit
        )
        val dao = AccountDao()
        dao.insert(checkingAccount)
    }

    fun addSavingAccount(groups: List<Group>) {
        val savingAccount = SavingsAccount(
            name = this.name,
            description = this.description,
            position = groups.find { it.id == this.group.id }!!.accounts.size + 1,
            icon = this.icon,
            balance = this.openBalance,
            group = this.group,
            openBalance = this.openBalance
        )
        val dao = AccountDao()
        dao.insert(savingAccount)
    }

    fun addCreditCardAccount(groups: List<Group>) {
        val creditCardAccount = CreditCardAccount(
            icon = this.icon,
            name = this.name,
            balance = 0.0,
            creditLimit = this.limit,
            closingDay = closingDay,
            dueDay = dueDay,
            group = this.group,
            description = this.description,
            position = groups.find { it.id == this.group.id }!!.accounts.size + 1
        )
        val dao = AccountDao()
        dao.insert(creditCardAccount)
    }
}