package application.party.usecases

import domain.entity.Party
import infrastructure.config.transactional

class ReassignAndDeletePartyUseCase {
    fun execute(from: Party, to: Party) {
        transactional { session ->
            val managedTo = session.merge(to)
            val managedFrom = session.merge(from)
            session.createMutationQuery(
                "UPDATE Transaction t SET t.party = :to WHERE t.party = :from"
            ).setParameter("to", managedTo)
             .setParameter("from", managedFrom)
             .executeUpdate()
            session.remove(managedFrom)
        }
    }
}
