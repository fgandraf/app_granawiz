package application.party.usecases

import domain.entity.PartyName
import infrastructure.repository.PartyRepository

class DeleteNameUseCase(private val partyRepository: PartyRepository = PartyRepository()) {


    fun execute(partyName: PartyName) {
        partyRepository.deleteName(partyName)
    }


}