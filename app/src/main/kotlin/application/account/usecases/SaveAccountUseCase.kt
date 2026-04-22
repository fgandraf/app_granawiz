package application.account.usecases

import domain.entity.account.BankAccount
import infrastructure.repository.AccountRepository

class SaveAccountUseCase(private val dao: AccountRepository = AccountRepository()) {

    fun execute(account: BankAccount) {

        if (account.id == 0L) dao.insert(account)
        else dao.update(account)

    }

}