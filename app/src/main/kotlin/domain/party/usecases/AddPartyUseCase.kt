package domain.party.usecases

import core.entity.Party
import core.enums.PartyType
import infra.dao.PartyDao

class AddPartyUseCase(private val partyDao: PartyDao = PartyDao()) {


    fun execute(name: String, type: PartyType): Pair<String, Party?> {

        val existingParty = partyDao.getPartyByName(name)
        if (existingParty != null)
            return Pair("Já existe um \"${name}\" no banco de dados.", null)

        val newParty = Party(name = name, type = type)
        partyDao.insert(newParty)

        return Pair("Associação vinculada com sucesso!", newParty)
    }


}