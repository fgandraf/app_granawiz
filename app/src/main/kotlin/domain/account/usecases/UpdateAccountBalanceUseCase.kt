package domain.account.usecases

import infra.dao.AccountDao

class UpdateAccountBalanceUseCase(private val dao: AccountDao = AccountDao()) {

    fun execute(accountId: Long, balance: Double) {

        val updatedAccount = dao.getAccountById(accountId) ?: return

        updatedAccount.balance = balance
        dao.update(updatedAccount)

    }

}