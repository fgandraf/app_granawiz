package application.category.usecases

import domain.contracts.ICategoryRepository
import domain.entity.Category
class UpdateCategoryUseCase(private val categoryRepository: ICategoryRepository) {

    fun execute(category: Category, name: String? = null, icon: String? = null) {
        val updatedCategory = Category(id = category.id, name = name ?: category.name, type = category.type, icon = icon ?: category.icon, subcategories = category.subcategories)
        categoryRepository.update(updatedCategory)
    }
}