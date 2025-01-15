package domain.account.usecases

import core.entity.account.BankAccount
import infra.dao.AccountDao

class DeleteAccountUseCase(private val accountDao: AccountDao = AccountDao()) {


    fun execute(account: BankAccount) {
        accountDao.delete(account)
    }


}