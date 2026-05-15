package application.account

import domain.contracts.IAccountRepository
import domain.entity.Group
import domain.entity.account.BankAccount
import application.account.usecases.MoveAccountPositionUseCase
import application.account.usecases.SaveAccountUseCase
import application.account.usecases.UpdateAccountBalanceUseCase

class AccountHandler(
    private val accountRepository: IAccountRepository,
    private val moveAccountPosition: MoveAccountPositionUseCase,
    private val updateAccountBalance: UpdateAccountBalanceUseCase,
    private val saveAccount: SaveAccountUseCase,
) {

    fun deleteAccount(account: BankAccount) = accountRepository.delete(account)
    fun moveAccountPosition(groups: List<Group>, account: BankAccount, direction: Int) =
        moveAccountPosition.execute(groups, account, direction)
    fun updateBalance(accountId: Long, balance: Double) = updateAccountBalance.execute(accountId, balance)
    fun saveAccount(account: BankAccount) = saveAccount.execute(account)
}
