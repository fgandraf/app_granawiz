package application.account.usecases

import domain.contracts.IAccountRepository
import domain.entity.account.BankAccount
class SaveAccountUseCase(private val dao: IAccountRepository) {

    fun execute(account: BankAccount) {

        if (account.id == 0L) dao.insert(account)
        else dao.update(account)

    }

}