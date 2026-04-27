package application.category.usecases

import domain.contracts.ICategoryRepository
import domain.contracts.ITransactionRepository
import domain.entity.Category
import infrastructure.repository.CategoryRepository
import infrastructure.repository.TransactionRepository

class DeleteCategoryUseCase(
    private val categoryRepository: ICategoryRepository = CategoryRepository(),
    private val transactionRepository: ITransactionRepository = TransactionRepository(),
) {

    fun execute(category: Category) {
        val transactions = transactionRepository.getAllByCategory(category)
        if (transactions.isNotEmpty()) {
            val uncategorized = categoryRepository.findByNameAndType("Sem categoria", category.type)
                ?: Category(type = category.type, name = "Sem categoria", icon = "question-mark").also {
                    categoryRepository.insert(it)
                }
            transactions.forEach { transaction ->
                transactionRepository.update(transaction.copy(category = uncategorized, subcategory = null))
            }
        }
        categoryRepository.delete(category)
    }

}