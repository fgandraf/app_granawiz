package application.party.usecases

import domain.contracts.IPartyRepository
import domain.entity.PartyName
import infrastructure.repository.PartyRepository

class DeleteNameUseCase(private val partyRepository: IPartyRepository = PartyRepository()) {


    fun execute(partyName: PartyName) {
        partyRepository.deleteName(partyName)
    }


}