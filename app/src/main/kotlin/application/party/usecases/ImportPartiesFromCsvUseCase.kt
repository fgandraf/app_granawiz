package application.party.usecases

import domain.contracts.IPartyRepository
import domain.entity.Party
import domain.entity.PartyName
import domain.enums.PartyType
import java.io.File

class ImportPartiesFromCsvUseCase(private val partyRepository: IPartyRepository) {

    data class Report(val imported: Int, val skipped: Int, val errors: Int)

    fun execute(file: File, type: PartyType): Report {
        var imported = 0
        var skipped = 0
        var errors = 0

        val lines = file.bufferedReader(Charsets.UTF_8).use { it.readLines() }
        if (lines.size <= 1) return Report(0, 0, 0)

        lines.drop(1).forEach { line ->
            if (line.isBlank()) return@forEach
            runCatching {
                val cols = parseLine(line)
                val partyName = cols.getOrNull(0)?.trim() ?: return@runCatching
                if (partyName.isBlank()) return@runCatching

                val altNames = cols.getOrNull(2)
                    ?.trim()
                    ?.split("|")
                    ?.map { it.trim() }
                    ?.filter { it.isNotBlank() }
                    ?: emptyList()

                val existing = partyRepository.getPartyByName(partyName)
                val party: Party
                if (existing != null) {
                    skipped++
                    party = existing
                } else {
                    partyRepository.insert(Party(name = partyName, type = type))
                    party = partyRepository.getPartyByName(partyName)!!
                    imported++
                }

                altNames.forEach { altName ->
                    if (partyRepository.getPartyNameByName(altName) == null) {
                        partyRepository.insertName(PartyName(name = altName, party = party))
                    }
                }
            }.onFailure { errors++ }
        }

        return Report(imported, skipped, errors)
    }

    private fun parseLine(line: String): List<String> {
        val result = mutableListOf<String>()
        val current = StringBuilder()
        var inQuotes = false
        var i = 0
        while (i < line.length) {
            val c = line[i]
            when {
                inQuotes && c == '"' && i + 1 < line.length && line[i + 1] == '"' -> {
                    current.append('"'); i++
                }
                c == '"' -> inQuotes = !inQuotes
                c == ';' && !inQuotes -> { result.add(current.toString()); current.clear() }
                else -> current.append(c)
            }
            i++
        }
        result.add(current.toString())
        return result
    }
}
