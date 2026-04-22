package application.party.usecases

import domain.entity.Party
import domain.entity.PartyName
import infrastructure.repository.PartyRepository

class FetchNamesUseCase(private val partyRepository: PartyRepository = PartyRepository()) {


    fun execute(party: Party?): List<PartyName> {
        if (party == null)
            return emptyList()

        val parties = partyRepository.getAll(party.type)
        return parties.find { x -> x.id == party.id }?.partiesNames!!
    }


}