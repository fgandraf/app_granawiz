package model.dao

import config.HibernateUtil
import model.entity.Tag

class TagDao {

    private val sessionFactory = HibernateUtil.getSessionFactory()

    fun getAll() : List<Tag> {
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


    fun delete(tag: Tag) {
        val session = sessionFactory.openSession()

        session.beginTransaction()
        session.remove(tag)
        session.transaction.commit()

        session.close()
    }

    fun update(tag: Tag) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.merge(tag)
        session.transaction.commit()
        session.close()
    }

    fun insert(tag: Tag) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.persist(tag)
        session.transaction.commit()
        session.close()
    }
}