package domain.party.usecases

import core.entity.Party
import core.entity.PartyName
import infra.dao.PartyDao

class AddNameUseCase(private val partyDao: PartyDao = PartyDao()) {

    fun execute(newName: String, party: Party): Pair<String, PartyName?> {

        val existingName = partyDao.getPartyNameByName(newName)
        if (existingName != null)
            return Pair("O nome \"${newName}\" já está associado à \"${existingName.party.name}\".", null)

        val newPartyName = PartyName(name = newName, party = party)
        partyDao.insertName(newPartyName)
        return Pair("Associação vinculada com sucesso!", newPartyName)
    }

}