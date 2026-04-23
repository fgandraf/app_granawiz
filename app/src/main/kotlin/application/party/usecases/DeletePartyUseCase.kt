package application.party.usecases

import domain.contracts.IPartyRepository
import domain.entity.Party
import infrastructure.repository.PartyRepository

class DeletePartyUseCase(private val partyRepository: IPartyRepository = PartyRepository()) {


    fun execute(party: Party) {
        partyRepository.delete(party)
    }

}