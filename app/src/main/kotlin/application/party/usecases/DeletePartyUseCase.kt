package application.party.usecases

import domain.contracts.IPartyRepository
import domain.entity.Party
import infrastructure.repository.PartyRepository

class DeletePartyUseCase(private val partyRepository: IPartyRepository = PartyRepository()) {


    fun execute(party: Party): String? {
        if (partyRepository.hasTransactions(party))
            return "Este registro possui transações vinculadas e não pode ser excluído."
        partyRepository.delete(party)
        return null
    }

}