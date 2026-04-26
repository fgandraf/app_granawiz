package application.importStatement.usecases

import domain.contracts.IPartyRepository
import domain.entity.Party
import domain.enums.PartyType
import domain.enums.TransactionType
import domain.structs.ParsedEntry
import infrastructure.repository.PartyRepository

class ResolveOrCreatePartyUseCase(
    private val partyRepository: IPartyRepository = PartyRepository(),
    private val resolveParty: ResolvePartyByNameUseCase = ResolvePartyByNameUseCase(),
) {
    fun execute(entry: ParsedEntry): Party {
        entry.party?.let { return it }

        val name = entry.customPartyName?.takeIf { it.isNotBlank() }
            ?: entry.rawCounterpartyName.trim()

        resolveParty.execute(name)?.let { return it }

        val type = if (entry.type == TransactionType.GAIN) PartyType.PAYER else PartyType.RECEIVER
        val newParty = Party(name = name, type = type)
        partyRepository.insert(newParty)
        return newParty
    }
}
