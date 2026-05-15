package infrastructure.repository

import domain.contracts.ICategoryRepository
import domain.entity.Category
import domain.entity.Subcategory
import domain.enums.CategoryType
import infrastructure.config.HibernateUtil
import org.hibernate.Hibernate

class CategoryRepository : ICategoryRepository {

    private val sessionFactory = HibernateUtil.getSessionFactory()

    override fun getAll(type: CategoryType): List<Category> {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val criteriaBuilder = session.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(Category::class.java)
        val root = criteriaQuery.from(Category::class.java)

        val predicate = criteriaBuilder.equal(root.get<CategoryType>("type"), type)
        criteriaQuery.select(root).where(predicate)

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

    override fun findByNameAndType(name: String, type: CategoryType): Category? {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val cb = session.criteriaBuilder
        val cq = cb.createQuery(Category::class.java)
        val root = cq.from(Category::class.java)
        cq.where(
            cb.and(
                cb.equal(root.get<String>("name"), name),
                cb.equal(root.get<CategoryType>("type"), type)
            )
        )
        val result = session.createQuery(cq).uniqueResultOptional().orElse(null)
        session.transaction.commit()
        session.close()
        return result
    }

    override fun delete(category: Category) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.remove(category)
        session.transaction.commit()
        session.close()
    }

    override fun deleteSubcategory(subcategory: Subcategory) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.remove(subcategory)
        session.transaction.commit()
        session.close()
    }

    override fun update(category: Category) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.merge(category)
        session.transaction.commit()
        session.close()
    }

    override fun updateSubcategory(subcategory: Subcategory) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.merge(subcategory)
        session.transaction.commit()
        session.close()
    }

    override fun insert(category: Category) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.persist(category)
        session.transaction.commit()
        session.close()
    }

    override fun insertSubcategory(subcategory: Subcategory) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.persist(subcategory)
        session.transaction.commit()
        session.close()

    }
}