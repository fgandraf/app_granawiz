package application.party.usecases

import domain.entity.Party
import java.io.File

class ExportPartiesToCsvUseCase {

    private val delimiter = ';'
    private val headers = listOf("Nome", "Tipo", "NomesAlternativos")

    fun execute(parties: List<Party>, file: File) {
        file.bufferedWriter(Charsets.UTF_8).use { writer ->
            writer.append(headers.joinToString(delimiter.toString()))
            writer.append('\n')

            parties.sortedBy { it.name }.forEach { party ->
                val altNames = party.partiesNames.joinToString("|") { it.name }
                val row = listOf(
                    party.name,
                    party.type.name,
                    altNames
                ).joinToString(delimiter.toString()) { escape(it) }

                writer.append(row)
                writer.append('\n')
            }
        }
    }

    private fun escape(field: String): String {
        val needsQuoting = field.any { it == delimiter || it == ',' || it == '"' || it == '\n' || it == '\r' }
        return if (needsQuoting) "\"${field.replace("\"", "\"\"")}\"" else field
    }
}
