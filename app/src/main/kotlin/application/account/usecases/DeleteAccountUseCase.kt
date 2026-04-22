package application.account.usecases

import domain.entity.account.BankAccount
import infrastructure.repository.AccountRepository

class DeleteAccountUseCase(private val accountRepository: AccountRepository = AccountRepository()) {


    fun execute(account: BankAccount) {
        accountRepository.delete(account)
    }


}