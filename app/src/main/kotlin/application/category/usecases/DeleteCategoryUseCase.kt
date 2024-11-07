package application.category.usecases

import domain.contracts.ICategoryRepository
import domain.entity.Category
import infrastructure.config.transactional
class DeleteCategoryUseCase(
    private val categoryRepository: ICategoryRepository,
) {
    fun execute(category: Category) {
        val existingUncategorized = categoryRepository.findByNameAndType("Sem categoria", category.type)
        transactional { session ->
            val uncategorized = if (existingUncategorized != null) {
                session.merge(existingUncategorized)
            } else {
                val newCat = Category(type = category.type, name = "Sem categoria", icon = "question-mark")
                session.persist(newCat)
                newCat
            }
            val managedCategory = session.merge(category)
            session.createMutationQuery(
                "UPDATE Transaction t SET t.category = :uc, t.subcategory = null WHERE t.category = :cat"
            ).setParameter("uc", uncategorized)
             .setParameter("cat", managedCategory)
             .executeUpdate()
            session.remove(managedCategory)
        }
    }
}
