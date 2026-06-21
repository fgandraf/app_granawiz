package application.account

import domain.contracts.IAccountRepository
import domain.contracts.IScheduleRepository
import domain.contracts.ITransactionRepository
import domain.entity.Group
import domain.entity.account.BankAccount
import application.account.usecases.MoveAccountPositionUseCase
import application.account.usecases.SaveAccountUseCase
import application.account.usecases.UpdateAccountBalanceUseCase

class AccountHandler(
    private val accountRepository: IAccountRepository,
    private val transactionRepository: ITransactionRepository,
    private val scheduleRepository: IScheduleRepository,
    private val moveAccountPosition: MoveAccountPositionUseCase,
    private val updateAccountBalance: UpdateAccountBalanceUseCase,
    private val saveAccount: SaveAccountUseCase,
) {

    fun deleteAccount(account: BankAccount) {
        transactionRepository.getAllByAccount(account).forEach { transactionRepository.delete(it) }
        scheduleRepository.getAllByAccount(account).forEach { scheduleRepository.delete(it) }
        accountRepository.delete(account)
    }
    fun moveAccountPosition(groups: List<Group>, account: BankAccount, direction: Int) =
        moveAccountPosition.execute(groups, account, direction)
    fun updateBalance(accountId: Long, balance: Double) = updateAccountBalance.execute(accountId, balance)
    fun saveAccount(account: BankAccount) = saveAccount.execute(account)
}
