package application.account.usecases

import infrastructure.repository.AccountRepository

class UpdateAccountBalanceUseCase(private val dao: AccountRepository = AccountRepository()) {

    fun execute(accountId: Long, balance: Double) {

        val updatedAccount = dao.getAccountById(accountId) ?: return

        updatedAccount.balance = balance
        dao.update(updatedAccount)

    }

}