package application.party.usecases

import domain.contracts.IPartyRepository
import domain.entity.Party
import domain.entity.PartyName
class FetchNamesUseCase(private val partyRepository: IPartyRepository) {


    fun execute(party: Party?): List<PartyName> {
        if (party == null)
            return emptyList()

        val parties = partyRepository.getAll(party.type)
        return parties.find { x -> x.id == party.id }?.partiesNames!!
    }


}