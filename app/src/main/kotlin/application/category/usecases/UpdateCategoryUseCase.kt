package application.category.usecases

import domain.contracts.ICategoryRepository
import domain.entity.Category
import infrastructure.repository.CategoryRepository

class UpdateCategoryUseCase(private val categoryRepository: ICategoryRepository = CategoryRepository()) {

    fun execute(category: Category, name: String? = null, icon: String? = null) {
        val updatedCategory = Category(id = category.id, name = name ?: category.name, type = category.type, icon = icon ?: category.icon, subcategories = category.subcategories)
        categoryRepository.update(updatedCategory)
    }
}