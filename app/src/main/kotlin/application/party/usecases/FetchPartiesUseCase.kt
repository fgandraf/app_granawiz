package application.party.usecases

import domain.entity.Party
import domain.enums.PartyType
import infrastructure.repository.PartyRepository

class FetchPartiesUseCase(private val partyRepository: PartyRepository = PartyRepository()) {

    fun execute(type: PartyType): List<Party> {
        return partyRepository.getAll(type)
    }

}