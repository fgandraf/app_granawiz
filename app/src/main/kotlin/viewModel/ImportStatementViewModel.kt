package viewModel

import application.importStatement.ImportHandler
import application.importStatement.LogLevel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import domain.entity.account.BankAccount
import domain.structs.ParsedEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.io.File

data class WizardLogLine(val level: LogLevel, val text: String)

class ImportStatementViewModel(
    private val importHandler: ImportHandler = ImportHandler(),
) {

    var currentStep by mutableStateOf(0)
    var selectedFile by mutableStateOf<File?>(null)
    var account by mutableStateOf<BankAccount?>(null)

    val parsedEntries = MutableStateFlow<List<ParsedEntry>>(emptyList())
    val parseLog = MutableStateFlow<List<WizardLogLine>>(emptyList())
    val isParsing = MutableStateFlow(false)
    val parseFailed = MutableStateFlow(false)

    val importLog = MutableStateFlow<List<WizardLogLine>>(emptyList())
    val isImporting = MutableStateFlow(false)
    val importDone = MutableStateFlow(false)
    val importFailed = MutableStateFlow(false)

    private var hasParsedFor: Pair<File, BankAccount>? = null

    suspend fun parseSelectedFile() {
        val file = selectedFile ?: return
        val acct = account ?: return
        if (hasParsedFor == (file to acct) && parsedEntries.value.isNotEmpty()) return
        isParsing.value = true
        parseFailed.value = false
        parseLog.value = emptyList()
        val result = withContext(Dispatchers.IO) {
            runCatching {
                importHandler.parseAndResolve(file, acct) { msg, lvl ->
                    parseLog.value = parseLog.value + WizardLogLine(lvl, msg)
                }
            }
        }
        result.onSuccess { entries ->
            parsedEntries.value = entries
            hasParsedFor = file to acct
            if (entries.isEmpty()) parseFailed.value = true
        }.onFailure { e ->
            parseLog.value = parseLog.value +
                WizardLogLine(LogLevel.ERROR, "Falha: ${e.message ?: "erro desconhecido"}")
            parseFailed.value = true
        }
        isParsing.value = false
    }

    suspend fun executeImport() {
        val acct = account ?: run {
            importLog.value = listOf(WizardLogLine(LogLevel.ERROR, "Conta não selecionada."))
            importFailed.value = true
            return
        }
        isImporting.value = true
        importLog.value = emptyList()
        importFailed.value = false
        importDone.value = false
        val result = withContext(Dispatchers.IO) {
            runCatching {
                importHandler.executeImport(parsedEntries.value, acct) { msg, lvl ->
                    importLog.value = importLog.value + WizardLogLine(lvl, msg)
                }
            }
        }
        result.onSuccess {
            importDone.value = true
        }.onFailure { e ->
            importLog.value = importLog.value +
                WizardLogLine(LogLevel.ERROR, "Erro inesperado: ${e.message ?: "erro desconhecido"}")
            importFailed.value = true
        }
        isImporting.value = false
    }

    fun updateEntry(rowId: String, updater: (ParsedEntry) -> ParsedEntry) {
        parsedEntries.value = parsedEntries.value.map {
            if (it.rowId == rowId) updater(it) else it
        }
    }

    fun removeEntry(rowId: String) {
        parsedEntries.value = parsedEntries.value.filter { it.rowId != rowId }
    }

    fun clearAll() {
        currentStep = 0
        selectedFile = null
        parsedEntries.value = emptyList()
        parseLog.value = emptyList()
        isParsing.value = false
        parseFailed.value = false
        importLog.value = emptyList()
        isImporting.value = false
        importDone.value = false
        importFailed.value = false
        hasParsedFor = null
        account = null
    }
}
