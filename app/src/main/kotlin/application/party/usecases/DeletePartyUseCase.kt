package application.party.usecases

import domain.entity.Party
import infrastructure.repository.PartyRepository

class DeletePartyUseCase(private val partyRepository: PartyRepository = PartyRepository()) {


    fun execute(party: Party) {
        partyRepository.delete(party)
    }

}