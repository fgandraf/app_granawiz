package domain.contracts

import domain.entity.Category
import domain.entity.Subcategory
import domain.enums.CategoryType

interface ICategoryRepository {

    fun getAll(type: CategoryType) : List<Category>

    fun findByNameAndType(name: String, type: CategoryType): Category?

    fun delete(category: Category)

    fun deleteSubcategory(subcategory: Subcategory)

    fun update(category: Category)

    fun updateSubcategory(subcategory: Subcategory)

    fun insert(category: Category)

    fun insertSubcategory(subcategory: Subcategory)
}