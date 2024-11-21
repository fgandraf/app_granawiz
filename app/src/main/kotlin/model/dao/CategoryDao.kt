package model.dao

import config.HibernateUtil
import model.entity.Category
import model.entity.Subcategory
import model.enums.CategoryType
import org.hibernate.Hibernate

class CategoryDao {

    private val sessionFactory = HibernateUtil.getSessionFactory()

    fun getAll(type: CategoryType) : List<Category> {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val criteriaBuilder = session.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(Category::class.java)
        val root = criteriaQuery.from(Category::class.java)

        // Adicionando a condição para o atributo "category_type" ser "EXPENSE"
        val predicate = criteriaBuilder.equal(root.get<CategoryType>("type"), type)
        criteriaQuery.select(root).where(predicate)

        // Ordenação por "name"
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get<String>("name")))

        val categories = session.createQuery(criteriaQuery).resultList
        categories.forEach { category ->
            Hibernate.initialize(category.subcategories)
            category.subcategories.sortBy { it.name }
        }
        session.transaction.commit()
        session.close()
        return categories
    }

    fun delete(category: Category) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.remove(category)
        session.transaction.commit()
        session.close()
    }

    fun deleteSubcategory(subcategory: Subcategory) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.remove(subcategory)
        session.transaction.commit()
        session.close()
    }

    fun update(category: Category) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.merge(category)
        session.transaction.commit()
        session.close()
    }

    fun updateSubcategory(subcategory: Subcategory) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.merge(subcategory)
        session.transaction.commit()
        session.close()
    }

    fun insert(category: Category) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.persist(category)
        session.transaction.commit()
        session.close()
    }

    fun insertSubcategory(subcategory: Subcategory) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.persist(subcategory)
        session.transaction.commit()
        session.close()

    }
}