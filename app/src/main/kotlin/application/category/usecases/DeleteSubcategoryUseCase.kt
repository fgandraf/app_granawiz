package application.category.usecases

import domain.contracts.ICategoryRepository
import domain.contracts.ITransactionRepository
import domain.entity.Subcategory
import infrastructure.repository.CategoryRepository
import infrastructure.repository.TransactionRepository

class DeleteSubcategoryUseCase(
    private val categoryRepository: ICategoryRepository = CategoryRepository(),
    private val transactionRepository: ITransactionRepository = TransactionRepository(),
) {

    fun execute(subcategory: Subcategory) {
        transactionRepository.getAllBySubcategory(subcategory).forEach { transaction ->
            transactionRepository.update(transaction.copy(subcategory = null))
        }
        categoryRepository.deleteSubcategory(subcategory)
    }

}