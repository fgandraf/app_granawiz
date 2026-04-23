package application.party.usecases

import domain.contracts.IPartyRepository
import domain.entity.Party
import domain.entity.PartyName
import infrastructure.repository.PartyRepository

class AddNameUseCase(private val partyRepository: IPartyRepository = PartyRepository()) {

    fun execute(newName: String, party: Party): Pair<String, PartyName?> {

        val existingName = partyRepository.getPartyNameByName(newName)
        if (existingName != null)
            return Pair("O nome \"${newName}\" já está associado à \"${existingName.party.name}\".", null)

        val newPartyName = PartyName(name = newName, party = party)
        partyRepository.insertName(newPartyName)
        return Pair("Associação vinculada com sucesso!", newPartyName)
    }

}