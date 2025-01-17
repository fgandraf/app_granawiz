package domain.account.usecases

import core.entity.account.BankAccount
import infra.dao.AccountDao

class SaveAccountUseCase(private val dao: AccountDao = AccountDao()) {

    fun execute(account: BankAccount) {

        if (account.id == 0L) dao.insert(account)
        else dao.update(account)

    }

}