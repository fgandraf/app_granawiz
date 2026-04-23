package application.party.usecases

import domain.contracts.IPartyRepository
import domain.entity.PartyName
import infrastructure.repository.PartyRepository

class UpdateNameUseCase(private val partyRepository: IPartyRepository = PartyRepository()) {


    fun execute(partyName: PartyName, name: String): Pair<String, PartyName?> {

        val existingName = partyRepository.getPartyNameByName(name)
        if (existingName != null)
            return Pair("O nome \"${name}\" já está vinculado à \"${existingName.party.name}\".", null)

        val updatedPartyName = PartyName(id = partyName.id, name = name, party = partyName.party)
        return Pair("Associação vinculada com sucesso!", updatedPartyName)
    }


}