package domain.party.usecases

import core.entity.PartyName
import infra.dao.PartyDao

class UpdateNameUseCase(private val partyDao: PartyDao = PartyDao()) {


    fun execute(partyName: PartyName, name: String): Pair<String, PartyName?> {

        val existingName = partyDao.getPartyNameByName(name)
        if (existingName != null)
            return Pair("O nome \"${name}\" já está vinculado à \"${existingName.party.name}\".", null)

        val updatedPartyName = PartyName(id = partyName.id, name = name, party = partyName.party)
        return Pair("Associação vinculada com sucesso!", updatedPartyName)
    }


}