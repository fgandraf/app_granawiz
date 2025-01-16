package infra.dao

import core.contracts.IPartyDao
import core.entity.Party
import core.entity.PartyName
import core.enums.PartyType
import infra.config.HibernateUtil

class PartyDao : IPartyDao {

    private val sessionFactory = HibernateUtil.getSessionFactory()

    override fun getAll(type: PartyType) : List<Party> {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val criteriaBuilder = session.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(Party::class.java)
        val root = criteriaQuery.from(Party::class.java)

        val predicate = criteriaBuilder.equal(root.get<PartyType>("type"), type)
        criteriaQuery.select(root).where(predicate)

        criteriaQuery.orderBy(criteriaBuilder.asc(root.get<Int>("name")))
        val parties = session.createQuery(criteriaQuery).resultList
        parties.forEach { party -> party.partiesNames.sortBy { it.name } }
        session.transaction.commit()
        session.close()
        return parties
    }

    override fun getPartyNameByName(name: String): PartyName? {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val criteriaBuilder = session.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(PartyName::class.java)
        val root = criteriaQuery.from(PartyName::class.java)
        criteriaQuery.where(criteriaBuilder.equal(root.get<String>("name"), name))
        val query = session.createQuery(criteriaQuery)
        val partyName = query.resultList.firstOrNull()
        session.transaction.commit()
        session.close()
        return partyName
    }

    override fun getPartyByName(name: String): Party? {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        val criteriaBuilder = session.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(Party::class.java)
        val root = criteriaQuery.from(Party::class.java)
        criteriaQuery.where(criteriaBuilder.equal(root.get<String>("name"), name))
        val query = session.createQuery(criteriaQuery)
        val party = query.resultList.firstOrNull()
        session.transaction.commit()
        session.close()
        return party
    }


    override fun delete(party: Party) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.remove(party)
        session.transaction.commit()
        session.close()
    }

    override fun deleteName(partyName: PartyName) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.remove(partyName)
        session.transaction.commit()
        session.close()
    }

    override fun update(party: Party) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.merge(party)
        session.transaction.commit()
        session.close()
    }

    override fun updateName(partyName: PartyName) {
        val session = sessionFactory.openSession()
        try {
            session.beginTransaction()
            session.merge(partyName)
            session.transaction.commit()
        } catch (e: Exception) {
            session.transaction.rollback()
            if (e.cause is org.hibernate.exception.ConstraintViolationException)
                println("Erro: O nome '${partyName.name}' já está associado à '${partyName.party.name}'.")
        } finally {
            session.close()
        }
    }

    override fun insert(party: Party) {
        val session = sessionFactory.openSession()
        session.beginTransaction()
        session.persist(party)
        session.transaction.commit()
        session.close()
    }

    override fun insertName(partyName: PartyName) {
        val session = sessionFactory.openSession()
        try {
            session.beginTransaction()
            session.persist(partyName)
            session.transaction.commit()
        } catch (e: Exception) {
            session.transaction.rollback()
            if (e.cause is org.hibernate.exception.ConstraintViolationException)
                println("Erro: O nome '${partyName.name}' já está associado à '${partyName.party.name}'.")
        } finally {
            session.close()
        }
    }
}