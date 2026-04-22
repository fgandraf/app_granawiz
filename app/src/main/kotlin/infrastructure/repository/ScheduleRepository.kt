package infrastructure.repository

import domain.contracts.IScheduleRepository
import domain.entity.Schedule
import domain.entity.account.BankAccount
import infrastructure.config.HibernateUtil
import org.hibernate.Hibernate
import java.time.LocalDateTime

class ScheduleRepository : IScheduleRepository {

    private val sessionFactory = HibernateUtil.getSessionFactory()

    override fun getAll(): List<Schedule> {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val criteriaBuilder = session.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(Schedule::class.java)
        val root = criteriaQuery.from(Schedule::class.java)
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get<LocalDateTime>("startDate")))
        val schedules = session.createQuery(criteriaQuery).resultList
        schedules.forEach { schedule ->
            Hibernate.initialize(schedule.party)
            Hibernate.initialize(schedule.account)
            Hibernate.initialize(schedule.category)
            Hibernate.initialize(schedule.subcategory)
            Hibernate.initialize(schedule.tags)
        }
        session.transaction.commit()
        session.close()
        return schedules
    }

    override fun getAllByAccount(account: BankAccount): List<Schedule> {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val criteriaBuilder = session.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(Schedule::class.java)
        val root = criteriaQuery.from(Schedule::class.java)
        criteriaQuery.where(criteriaBuilder.equal(root.get<BankAccount>("account"), account))
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get<LocalDateTime>("startDate")))
        val schedules = session.createQuery(criteriaQuery).resultList
        schedules.forEach { schedule ->
            Hibernate.initialize(schedule.party)
            Hibernate.initialize(schedule.account)
            Hibernate.initialize(schedule.category)
            Hibernate.initialize(schedule.subcategory)
            Hibernate.initialize(schedule.tags)
        }
        session.transaction.commit()
        session.close()
        return schedules
    }

    fun insert(schedule: Schedule): Schedule {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val saved = session.merge(schedule)
        session.transaction.commit()
        session.close()
        return saved
    }

    fun update(schedule: Schedule) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.merge(schedule)
        session.transaction.commit()
        session.close()
    }

    fun delete(schedule: Schedule) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.remove(session.merge(schedule))
        session.transaction.commit()
        session.close()
    }

}
