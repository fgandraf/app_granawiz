package view.modules.party.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import domain.enums.PartyType
import application.party.usecases.ImportPartiesFromCsvUseCase
import view.shared.*
import viewModel.PartyViewModel
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.time.LocalDate

@Composable
fun Body(
    partyType: PartyType,
) {
    val viewModel = remember(partyType) { PartyViewModel(partyType) }

    LaunchedEffect(viewModel) { viewModel.getParties() }

    val parties by viewModel.parties.collectAsState()
    val names by viewModel.partyNames.collectAsState()

    val selectedParty by viewModel.selectedParty.collectAsState()

    var addNameButton by remember { mutableStateOf(false) }
    var filterText by remember { mutableStateOf("") }
    val filteredParties = remember(parties, filterText) {
        if (filterText.isBlank()) parties
        else parties.filter { it.name.contains(filterText, ignoreCase = true) }
    }

    val scope = rememberCoroutineScope()
    var importReport by remember { mutableStateOf<ImportPartiesFromCsvUseCase.Report?>(null) }

    val strExportDialogTitle = stringResource(Res.string.party_export_dialog_title)
    val strImportDialogTitle = stringResource(Res.string.party_import_dialog_title)
    val strImportResultTitle = stringResource(Res.string.party_import_result_title)
    val strImportResultMessage = stringResource(Res.string.party_import_result_message)

    val defaultExportFileName = remember(partyType) {
        val prefix = if (partyType == PartyType.PAYER) "pagadores" else "recebedores"
        "${prefix}_${LocalDate.now()}.csv"
    }

    if (importReport != null) {
        val report = importReport!!
        SimpleAlertDialog(
            onDismissRequest = { importReport = null },
            title = strImportResultTitle,
            message = strImportResultMessage
                .replace("%1\$d", report.imported.toString())
                .replace("%2\$d", report.skipped.toString())
                .replace("%3\$d", report.errors.toString()),
        )
    }

    // EXTERNAL
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        // CONTENT BOX
        val corner = 10.dp
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .fillMaxWidth(0.75f)
                .border(0.5.dp, MaterialTheme.colors.onSurface, shape = RoundedCornerShape(corner))
                .clip(RoundedCornerShape(corner))
                .background(MaterialTheme.colors.background.copy(0.6f))
        ) {


            // PARTIES
            Column(modifier = Modifier.weight(1f).fillMaxHeight().padding(35.dp)) {
                SearchField(
                    modifier = Modifier.fillMaxWidth(),
                    value = filterText,
                    onValueChange = { filterText = it }
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DefaultButton(
                        text = stringResource(Res.string.`import`),
                        onClick = {
                            val dialog = FileDialog(null as Frame?, strImportDialogTitle, FileDialog.LOAD)
                            dialog.filenameFilter = java.io.FilenameFilter { _, name -> name.lowercase().endsWith(".csv") }
                            dialog.isVisible = true
                            val dir = dialog.directory
                            val name = dialog.file
                            dialog.dispose()
                            if (dir != null && name != null) {
                                scope.launch(Dispatchers.IO) {
                                    importReport = viewModel.importFromCsv(File(dir, name))
                                }
                            }
                        }
                    )
                    DefaultButton(
                        text = stringResource(Res.string.export),
                        onClick = {
                            val dialog = FileDialog(null as Frame?, strExportDialogTitle, FileDialog.SAVE)
                            dialog.file = defaultExportFileName
                            dialog.isVisible = true
                            val dir = dialog.directory
                            val name = dialog.file
                            dialog.dispose()
                            if (dir != null && name != null) {
                                val safeName = if (name.endsWith(".csv")) name else "$name.csv"
                                scope.launch(Dispatchers.IO) {
                                    viewModel.exportToCsv(parties, File(dir, safeName))
                                }
                            }
                        }
                    )
                }
                Spacer(Modifier.height(2.dp))
                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    val listState = rememberLazyListState()
                    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                        items(filteredParties, key = { it.id }) { item ->
                            PartyListItem(
                                viewModel,
                                selectedParty,
                                item
                            ) { addNameButton = true }
                        }
                        item { AddParty(viewModel) }
                    }
                    VerticalScrollbar(
                        adapter = rememberScrollbarAdapter(listState),
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }


            // DIVIDER
            Divider(modifier = Modifier.width(1.dp).fillMaxHeight(0.95f).background(MaterialTheme.colors.onSurface))


            // PARTYNAMES
            Box(modifier = Modifier.weight(1f).fillMaxHeight().padding(35.dp)) {
                if (addNameButton) {
                    TextNormal(text = stringResource(Res.string.form_field_associated_names))
                    val listState = rememberLazyListState()
                    LazyColumn(state = listState, modifier = Modifier.fillMaxSize().padding(top = 30.dp)) {
                        items(names, key = { it.id }) { item -> PartyNameListItem(viewModel, item) }
                        item { AddPartyName(viewModel) }
                    }
                    VerticalScrollbar(
                        adapter = rememberScrollbarAdapter(listState),
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }


        }
    }
}
