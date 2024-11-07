package application.party.usecases

import domain.contracts.IPartyRepository
import domain.entity.Party
class UpdatePartyUseCase(private val partyRepository: IPartyRepository) {


    fun execute(party: Party, name: String): Pair<String, Party?> {

        val existingParty = partyRepository.getPartyByName(name)
        if (existingParty != null)
            return Pair("Já existe um \"${name}\" no banco de dados.", null)

        val updatedParty = Party(id = party.id, name = name, type = party.type, partiesNames = party.partiesNames)
        partyRepository.update(updatedParty)
        return Pair("Associação vinculada com sucesso!", updatedParty)
    }


}