package domain.party.usecases

import core.entity.Party
import infra.dao.PartyDao

class UpdatePartyUseCase(private val partyDao: PartyDao = PartyDao()) {


    fun execute(party: Party, name: String): Pair<String, Party?> {

        val existingParty = partyDao.getPartyByName(name)
        if (existingParty != null)
            return Pair("Já existe um \"${name}\" no banco de dados.", null)

        val updatedParty = Party(id = party.id, name = name, type = party.type, partiesNames = party.partiesNames)
        partyDao.update(updatedParty)
        return Pair("Associação vinculada com sucesso!", updatedParty)
    }


}