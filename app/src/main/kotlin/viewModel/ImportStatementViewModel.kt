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

    private var hasParsedFor: File? = null

    suspend fun parseSelectedFile() {
        val file = selectedFile ?: return
        if (hasParsedFor == file && parsedEntries.value.isNotEmpty()) return
        isParsing.value = true
        parseFailed.value = false
        parseLog.value = emptyList()
        val result = withContext(Dispatchers.IO) {
            runCatching {
                importHandler.parseAndResolve(file) { msg, lvl ->
                    parseLog.value = parseLog.value + WizardLogLine(lvl, msg)
                }
            }
        }
        result.onSuccess { entries ->
            parsedEntries.value = entries
            hasParsedFor = file
            if (entries.isEmpty()) parseFailed.value = true
        }.onFailure { e ->
            parseLog.value = parseLog.value +
                WizardLogLine(LogLevel.ERROR, "Falha: ${e.message ?: "erro desconhecido"}")
            parseFailed.value = true
        }
        isParsing.value = false
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
        hasParsedFor = null
    }
}
