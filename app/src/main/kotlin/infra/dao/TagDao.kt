package infra.dao

import core.contracts.ITagDao
import core.entity.Tag
import infra.config.HibernateUtil

class TagDao : ITagDao {

    private val sessionFactory = HibernateUtil.getSessionFactory()

    override fun getAll() : List<Tag> {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val criteriaBuilder = session.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(Tag::class.java)
        val root = criteriaQuery.from(Tag::class.java)
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get<Int>("name")))
        val tags = session.createQuery(criteriaQuery).resultList
        session.transaction.commit()
        session.close()
        return tags
    }


    override fun delete(tag: Tag) {
        val session = sessionFactory.openSession()

        session.beginTransaction()
        session.remove(tag)
        session.transaction.commit()

        session.close()
    }

    override fun update(tag: Tag) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.merge(tag)
        session.transaction.commit()
        session.close()
    }

    override fun insert(tag: Tag) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.persist(tag)
        session.transaction.commit()
        session.close()
    }
}