package application.party.usecases

import domain.entity.Party
import domain.enums.PartyType
import infrastructure.repository.PartyRepository

class AddPartyUseCase(private val partyRepository: PartyRepository = PartyRepository()) {


    fun execute(name: String, type: PartyType): Pair<String, Party?> {

        val existingParty = partyRepository.getPartyByName(name)
        if (existingParty != null)
            return Pair("Já existe um \"${name}\" no banco de dados.", null)

        val newParty = Party(name = name, type = type)
        partyRepository.insert(newParty)

        return Pair("Associação vinculada com sucesso!", newParty)
    }


}