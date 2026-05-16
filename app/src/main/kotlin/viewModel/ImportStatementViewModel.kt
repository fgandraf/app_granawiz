package viewModel

import application.importStatement.ImportHandler
import infrastructure.di.ApplicationContainer
import application.importStatement.LogLevel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import domain.entity.account.BankAccount
import domain.structs.ParsedEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File

data class WizardLogLine(val level: LogLevel, val text: String)

class ImportStatementViewModel(
    private val importHandler: ImportHandler = ApplicationContainer.importHandler,
) {

    var currentStep by mutableStateOf(0)
    var selectedFile by mutableStateOf<File?>(null)
    var account by mutableStateOf<BankAccount?>(null)

    private val _parsedEntries = MutableStateFlow<List<ParsedEntry>>(emptyList())
    val parsedEntries: StateFlow<List<ParsedEntry>> = _parsedEntries.asStateFlow()

    private val _parseLog = MutableStateFlow<List<WizardLogLine>>(emptyList())
    val parseLog: StateFlow<List<WizardLogLine>> = _parseLog.asStateFlow()

    private val _isParsing = MutableStateFlow(false)
    val isParsing: StateFlow<Boolean> = _isParsing.asStateFlow()

    private val _parseFailed = MutableStateFlow(false)
    val parseFailed: StateFlow<Boolean> = _parseFailed.asStateFlow()

    private val _importLog = MutableStateFlow<List<WizardLogLine>>(emptyList())
    val importLog: StateFlow<List<WizardLogLine>> = _importLog.asStateFlow()

    private val _isImporting = MutableStateFlow(false)
    val isImporting: StateFlow<Boolean> = _isImporting.asStateFlow()

    private val _importDone = MutableStateFlow(false)
    val importDone: StateFlow<Boolean> = _importDone.asStateFlow()

    private val _importFailed = MutableStateFlow(false)
    val importFailed: StateFlow<Boolean> = _importFailed.asStateFlow()

    private var hasParsedFor: Pair<File, BankAccount>? = null

    suspend fun parseSelectedFile() {
        val file = selectedFile ?: return
        val acct = account ?: return
        if (hasParsedFor == (file to acct) && _parsedEntries.value.isNotEmpty()) return
        _isParsing.value = true
        _parseFailed.value = false
        _parseLog.value = emptyList()
        val result = withContext(Dispatchers.IO) {
            runCatching {
                importHandler.parseAndResolve(file, acct) { msg, lvl ->
                    _parseLog.value = _parseLog.value + WizardLogLine(lvl, msg)
                }
            }
        }
        result.onSuccess { entries ->
            _parsedEntries.value = entries
            hasParsedFor = file to acct
            if (entries.isEmpty()) _parseFailed.value = true
        }.onFailure { e ->
            _parseLog.value = _parseLog.value +
                WizardLogLine(LogLevel.ERROR, "Falha: ${e.message ?: "erro desconhecido"}")
            _parseFailed.value = true
        }
        _isParsing.value = false
    }

    suspend fun executeImport() {
        val acct = account ?: run {
            _importLog.value = listOf(WizardLogLine(LogLevel.ERROR, "Conta não selecionada."))
            _importFailed.value = true
            return
        }
        _isImporting.value = true
        _importLog.value = emptyList()
        _importFailed.value = false
        _importDone.value = false
        val result = withContext(Dispatchers.IO) {
            runCatching {
                importHandler.executeImport(_parsedEntries.value, acct) { msg, lvl ->
                    _importLog.value = _importLog.value + WizardLogLine(lvl, msg)
                }
            }
        }
        result.onSuccess {
            _importDone.value = true
        }.onFailure { e ->
            _importLog.value = _importLog.value +
                WizardLogLine(LogLevel.ERROR, "Erro inesperado: ${e.message ?: "erro desconhecido"}")
            _importFailed.value = true
        }
        _isImporting.value = false
    }

    fun updateEntry(rowId: String, updater: (ParsedEntry) -> ParsedEntry) {
        _parsedEntries.value = _parsedEntries.value.map {
            if (it.rowId == rowId) updater(it) else it
        }
    }

    fun removeEntry(rowId: String) {
        _parsedEntries.value = _parsedEntries.value.filter { it.rowId != rowId }
    }

    fun clearAll() {
        currentStep = 0
        selectedFile = null
        _parsedEntries.value = emptyList()
        _parseLog.value = emptyList()
        _isParsing.value = false
        _parseFailed.value = false
        _importLog.value = emptyList()
        _isImporting.value = false
        _importDone.value = false
        _importFailed.value = false
        hasParsedFor = null
        account = null
    }
}
