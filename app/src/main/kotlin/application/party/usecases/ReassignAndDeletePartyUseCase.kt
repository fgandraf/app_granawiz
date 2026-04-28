package application.party.usecases

import domain.contracts.IPartyRepository
import domain.entity.Party
import infrastructure.repository.PartyRepository

class ReassignAndDeletePartyUseCase(private val partyRepository: IPartyRepository = PartyRepository()) {
    fun execute(from: Party, to: Party) {
        partyRepository.reassignTransactions(from, to)
        partyRepository.delete(from)
    }
}
