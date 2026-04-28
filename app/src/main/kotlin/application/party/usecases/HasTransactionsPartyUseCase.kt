package application.party.usecases

import domain.contracts.IPartyRepository
import domain.entity.Party
import infrastructure.repository.PartyRepository

class HasTransactionsPartyUseCase(private val partyRepository: IPartyRepository = PartyRepository()) {
    fun execute(party: Party): Boolean = partyRepository.hasTransactions(party)
}
