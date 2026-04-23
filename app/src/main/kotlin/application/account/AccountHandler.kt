package application.account

import domain.entity.Group
import domain.entity.account.BankAccount
import application.account.usecases.*

class AccountHandler {

    private val deleteAccount = DeleteAccountUseCase()
    private val moveAccountPosition = MoveAccountPositionUseCase()
    private val updateAccountBalance = UpdateAccountBalanceUseCase()
    private val saveAccount = SaveAccountUseCase()

    fun deleteAccount(account: BankAccount) = deleteAccount.execute(account)
    fun moveAccountPosition(groups: List<Group>, account: BankAccount, direction: Int) =
        moveAccountPosition.execute(groups, account, direction)

    fun updateBalance(accountId: Long, balance: Double) {
        updateAccountBalance.execute(accountId, balance)
    }

    fun saveAccount(account: BankAccount) {
        saveAccount.execute(account)
    }

}