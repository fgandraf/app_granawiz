package application.account.usecases

import domain.contracts.IAccountRepository
class UpdateAccountBalanceUseCase(private val dao: IAccountRepository) {

    fun execute(accountId: Long, balance: Double) {

        val updatedAccount = dao.getAccountById(accountId) ?: return

        updatedAccount.balance = balance
        dao.update(updatedAccount)

    }

}