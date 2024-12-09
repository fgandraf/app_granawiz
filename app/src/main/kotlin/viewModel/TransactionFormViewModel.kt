package viewModel

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import core.entity.*
import core.entity.account.BankAccount
import core.enums.CategoryType
import core.enums.TransactionType
import infra.dao.CategoryDao
import kotlinx.coroutines.flow.MutableStateFlow
import view.theme.Lime800
import view.theme.Red800
import java.time.LocalDateTime
import kotlin.math.abs

class TransactionFormViewModel {

    var id by mutableStateOf(0L)
    var party by mutableStateOf(Party())
    var account by mutableStateOf(BankAccount())
    var category by mutableStateOf(Category())
    var subCategory by mutableStateOf<Subcategory?>(null)
    var tags: MutableSet<Tag>? = mutableSetOf()
    var date by mutableStateOf(LocalDateTime.now())
    var description by mutableStateOf("")
    var balance by mutableStateOf(0.0)
    var type by mutableStateOf<TransactionType>(TransactionType.NEUTRAL)

    fun initializeFromTransaction(transaction: Transaction){
        transaction.let {
            id = it.id
            party = it.party
            account = it.account
            category = it.category
            subCategory = it.subcategory ?: Subcategory()
            tags = it.tags
            date = it.date
            description = it.description
            balance = it.balance
            type = it.type
        }
    }

    fun updateBalance(value: String = ""){
        balance = if(value != "")
            value.replace(".", "").replace(",", ".").toDouble()
        else balance

        balance = if (type == TransactionType.EXPENSE && balance != 0.0) -abs(balance) else abs(balance)
    }


    val typeColor = derivedStateOf {
        when (type) {
            TransactionType.EXPENSE -> Red800
            TransactionType.GAIN -> Lime800
            TransactionType.NEUTRAL -> Color.Gray
        }
    }


    val typeLabel = derivedStateOf {
        when (type) {
            TransactionType.EXPENSE -> "Despesa"
            TransactionType.GAIN -> "Receita"
            else -> ""
        }
    }




    var categories by mutableStateOf(emptyList<Category>()); private set
    var subCategories by mutableStateOf(emptyList<Subcategory>()); private set

    private val _selectedCategory = MutableStateFlow(Category())
    fun selectCategory(category: Category) {
        _selectedCategory.value = category
    }

    private val categoryDao = CategoryDao()

    fun loadCategories() {
        val categoryType = if (type == TransactionType.EXPENSE) CategoryType.EXPENSE else CategoryType.INCOME
        categories = categoryDao.getAll(categoryType)
        subCategories = emptyList()
    }

    fun loadSubCategories(category: Category) {
        subCategories = categories.find{ x -> x == category}?.subcategories!!
    }

    fun deleteCategory(category: Category) {
        categoryDao.delete(category)
        loadCategories()
    }

    fun deleteSubcategory(subcategory: Subcategory) {
        val categoryId = subcategory.category.id
        categoryDao.deleteSubcategory(subcategory)

        loadCategories()
        subCategories = categories.find { x -> x.id == categoryId }?.subcategories ?: emptyList()
    }

    fun addCategory(name: String, icon: String) {
        val newCategory = Category(name = name, icon = icon, type = category.type)
        categoryDao.insert(newCategory)
        loadCategories()
    }

    fun addSubcategory(name: String) {
        val newSubcategory = Subcategory(name = name, category = _selectedCategory.value)
        categoryDao.insertSubcategory(newSubcategory)
        loadCategories()
        subCategories = categories.find { x -> x.id == newSubcategory.category.id }?.subcategories ?: emptyList()
    }

    fun updateCategory(category: Category, name: String, icon: String) {
        val updatedCategory = Category(id = category.id, name = name, type = category.type, icon = icon, subcategories = category.subcategories)
        categoryDao.update(updatedCategory)
        loadCategories()
    }

    fun updateSubcategory(subcategory: Subcategory, name: String) {
        val categoryId = subcategory.category.id
        val updatedSubcategory = Subcategory(subcategory.id, name, subcategory.category)
        categoryDao.updateSubcategory(updatedSubcategory)
        loadCategories()
        subCategories = categories.find { x -> x.id == categoryId }?.subcategories ?: emptyList()
    }


}