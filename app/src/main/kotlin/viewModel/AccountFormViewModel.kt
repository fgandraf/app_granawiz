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
import infra.dao.AccountDao

class AccountFormViewModel {

    private val accountDao = AccountDao()

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

    fun saveCheckingAccount(account: CheckingAccount? = null){
        if (account == null) {
            val newAccount = buildAccount(AccountType.CHECKING) as CheckingAccount
            accountDao.insert(newAccount)
        }
        else{
            val updatedAccount = buildAccount(AccountType.CHECKING, id = account.id, position = account.position) as CheckingAccount
            accountDao.update(updatedAccount)
        }
    }

    fun saveSavingAccount(account: SavingsAccount? = null){
        if (account == null) {
            val newAccount = buildAccount(AccountType.SAVINGS) as SavingsAccount
            accountDao.insert(newAccount)
        }
        else{
            val updatedAccount = buildAccount(AccountType.SAVINGS, id = account.id, position = account.position) as SavingsAccount
            accountDao.update(updatedAccount)
        }
    }

    fun saveCreditCardAccount(account: CreditCardAccount? = null){
        if (account == null) {
            val newAccount = buildAccount(AccountType.CREDIT_CARD) as CreditCardAccount
            accountDao.insert(newAccount)
        }
        else{
            val updatedAccount = buildAccount(AccountType.CREDIT_CARD, id = account.id, position = account.position) as CreditCardAccount
            accountDao.update(updatedAccount)
        }
    }


    private fun buildAccount(accountType: AccountType, id: Long? = null, position: Int? = null) : BankAccount{
         when(accountType){
            AccountType.CHECKING -> {
                return CheckingAccount(
                    id = id,
                    name = this.name,
                    description = this.description,
                    position = position ?: (this.group.accounts.size + 1),
                    icon = this.icon,
                    balance = this.openBalance,
                    group = this.group,
                    openBalance = this.openBalance,
                    overdraftLimit = this.limit
                )
            }
            AccountType.SAVINGS -> {
                return SavingsAccount(
                    id = id ?: 0L,
                    name = this.name,
                    description = this.description,
                    position = position ?: (this.group.accounts.size + 1),
                    icon = this.icon,
                    balance = this.openBalance,
                    group = this.group,
                    openBalance = this.openBalance
                )
            }
            AccountType.CREDIT_CARD -> {
                return CreditCardAccount(
                    id = id ?: 0L,
                    name = this.name,
                    description = this.description,
                    position = position ?: (this.group.accounts.size + 1),
                    icon = this.icon,
                    balance = 0.0,
                    group = this.group,
                    creditLimit = this.limit,
                    closingDay = this.closingDay,
                    dueDay = this.dueDay
                )
            }
        }
    }
}