package application.account.usecases

import domain.contracts.IAccountRepository
import domain.entity.account.BankAccount
import infrastructure.repository.AccountRepository

class DeleteAccountUseCase(private val accountRepository: IAccountRepository = AccountRepository()) {


    fun execute(account: BankAccount) {
        accountRepository.delete(account)
    }


}