package domain.transaction.usecases

import core.entity.Transaction
import infra.dao.TransactionDao
import viewModel.TransactionFormViewModel

class SaveTransactionUseCase(private val transactionDao: TransactionDao = TransactionDao()) {

    fun execute(viewModel: TransactionFormViewModel) {
        val transaction = Transaction(
            id = viewModel.id,
            party = viewModel.party.value!!,
            account = viewModel.account,
            category = viewModel.category.value!!,
            subcategory = viewModel.subCategory,
            tags = viewModel.tags.value,
            date = viewModel.date,
            description = viewModel.description,
            balance = viewModel.balance,
            type = viewModel.type,
        )

        if (transaction.id == 0L) transactionDao.insert(transaction)
        else transactionDao.update(transaction)

    }

}