package application.account.usecases

import domain.contracts.IAccountRepository
import infrastructure.repository.AccountRepository

class UpdateAccountBalanceUseCase(private val dao: IAccountRepository = AccountRepository()) {

    fun execute(accountId: Long, balance: Double) {

        val updatedAccount = dao.getAccountById(accountId) ?: return

        updatedAccount.balance = balance
        dao.update(updatedAccount)

    }

}