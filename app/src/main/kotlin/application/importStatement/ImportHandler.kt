package application.importStatement

import application.importStatement.usecases.ParseOfxFileUseCase
import application.importStatement.usecases.ResolvePartyByNameUseCase
import domain.structs.ParsedEntry
import java.io.File

enum class LogLevel { INFO, OK, WARN, ERROR }

class ImportHandler(
    private val parseOfxFile: ParseOfxFileUseCase = ParseOfxFileUseCase(),
    private val resolveParty: ResolvePartyByNameUseCase = ResolvePartyByNameUseCase(),
) {

    fun parseAndResolve(
        file: File,
        onLog: (String, LogLevel) -> Unit = { _, _ -> },
    ): List<ParsedEntry> {
        onLog("Iniciando análise do arquivo...", LogLevel.INFO)
        onLog("Arquivo: ${file.name}", LogLevel.INFO)
        val raw = parseOfxFile.execute(file, onLog)
        if (raw.isEmpty()) return emptyList()

        onLog("Resolvendo pagadores/beneficiários...", LogLevel.INFO)
        val resolved = raw.map { entry ->
            val p = resolveParty.execute(entry.rawCounterpartyName)
            entry.copy(party = p, needsNewParty = (p == null))
        }
        val matched = resolved.count { it.party != null }
        onLog("$matched correspondências encontradas, ${resolved.size - matched} sem cadastro.", LogLevel.OK)
        onLog("Análise concluída. Revise os dados na próxima etapa.", LogLevel.INFO)
        return resolved
    }
}
