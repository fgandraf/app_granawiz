package core.contracts

import core.entity.Category
import core.entity.Subcategory
import core.enums.CategoryType

interface ICategoryDao {

    fun getAll(type: CategoryType) : List<Category>

    fun delete(category: Category)

    fun deleteSubcategory(subcategory: Subcategory)

    fun update(category: Category)

    fun updateSubcategory(subcategory: Subcategory)

    fun insert(category: Category)

    fun insertSubcategory(subcategory: Subcategory)
}