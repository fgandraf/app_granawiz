package application.importStatement

import application.importStatement.usecases.CheckDuplicatesUseCase
import application.importStatement.usecases.ImportTransactionsUseCase
import application.importStatement.usecases.ParseOfxFileUseCase
import application.importStatement.usecases.ResolvePartyByNameUseCase
import domain.entity.account.BankAccount
import domain.structs.ParsedEntry
import java.io.File

enum class LogLevel { INFO, OK, WARN, ERROR }

class ImportHandler(
    private val parseOfxFile: ParseOfxFileUseCase = ParseOfxFileUseCase(),
    private val resolveParty: ResolvePartyByNameUseCase = ResolvePartyByNameUseCase(),
    private val checkDuplicates: CheckDuplicatesUseCase = CheckDuplicatesUseCase(),
    private val importTransactions: ImportTransactionsUseCase = ImportTransactionsUseCase(),
) {

    fun parseAndResolve(
        file: File,
        account: BankAccount,
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

        onLog("Verificando possíveis duplicações...", LogLevel.INFO)
        val withDuplicates = checkDuplicates.execute(resolved, account)
        val dupeCount = withDuplicates.count { it.isPossibleDuplicate }
        if (dupeCount > 0) {
            onLog("$dupeCount registro(s) podem ser duplicações de transações já cadastradas.", LogLevel.WARN)
        }

        onLog("Análise concluída. Revise os dados na próxima etapa.", LogLevel.INFO)
        return withDuplicates
    }

    fun executeImport(
        entries: List<ParsedEntry>,
        account: BankAccount,
        onLog: (String, LogLevel) -> Unit = { _, _ -> },
    ): ImportTransactionsUseCase.Report {
        onLog("Iniciando importação de ${entries.size} transação(ões)…", LogLevel.INFO)
        val report = importTransactions.execute(entries, account, onLog)
        val level = if (report.failed == 0) LogLevel.OK else LogLevel.WARN
        onLog("Concluído: ${report.imported} importada(s), ${report.failed} com falha.", level)
        return report
    }
}
