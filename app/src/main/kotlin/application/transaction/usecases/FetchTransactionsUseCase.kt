package application.transaction.usecases

import domain.contracts.ITransactionRepository
import domain.entity.Transaction
import domain.entity.account.BankAccount
class FetchTransactionsUseCase(private val transactionRepository: ITransactionRepository) {

    fun execute(account: BankAccount? = null): List<Transaction> {
        return if (account == null) transactionRepository.getAll()
        else transactionRepository.getAllByAccount(account)
    }

}