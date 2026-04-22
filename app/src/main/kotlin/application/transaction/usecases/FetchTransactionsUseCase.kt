package application.transaction.usecases

import domain.entity.Transaction
import domain.entity.account.BankAccount
import infrastructure.repository.TransactionRepository

class FetchTransactionsUseCase(private val transactionRepository: TransactionRepository = TransactionRepository()) {

    fun execute(account: BankAccount? = null): List<Transaction> {
        return if (account == null) transactionRepository.getAll()
        else transactionRepository.getAllByAccount(account)
    }

}