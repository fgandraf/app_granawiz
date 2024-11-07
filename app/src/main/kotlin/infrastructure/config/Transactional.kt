package infrastructure.config

import org.hibernate.Session

inline fun <T> transactional(block: (Session) -> T): T {
    val session = HibernateUtil.getSessionFactory().openSession()
    val tx = session.beginTransaction()
    return try {
        block(session).also { tx.commit() }
    } catch (e: Exception) {
        tx.rollback()
        throw e
    } finally {
        session.close()
    }
}
