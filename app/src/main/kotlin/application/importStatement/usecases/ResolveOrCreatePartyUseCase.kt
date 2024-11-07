package application.importStatement.usecases

import domain.contracts.IPartyRepository
import domain.entity.Party
import domain.enums.PartyType
import domain.enums.TransactionType
import domain.structs.ParsedEntry
class ResolveOrCreatePartyUseCase(
    private val partyRepository: IPartyRepository,
    private val resolveParty: ResolvePartyByNameUseCase,
) {
    fun execute(entry: ParsedEntry): Party {
        val expectedType = if (entry.type == TransactionType.GAIN) PartyType.PAYER else PartyType.RECEIVER

        entry.party?.let { return it }

        val name = entry.customPartyName?.takeIf { it.isNotBlank() }
            ?: entry.rawCounterpartyName.trim()

        resolveParty.execute(name, expectedType)?.let { return it }

        val newParty = Party(name = name, type = expectedType)
        partyRepository.insert(newParty)
        return newParty
    }
}
