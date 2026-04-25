package view.modules.importStatement.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import application.category.CategoryHandler
import application.party.PartyHandler
import domain.entity.Category
import domain.entity.Party
import domain.enums.CategoryType
import domain.enums.PartyType
import domain.enums.TransactionType
import domain.structs.ParsedEntry
import utils.toBrMoney
import view.shared.*
import viewModel.ImportStatementViewModel
import kotlin.math.abs

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WizardStepThree(
    viewModel: ImportStatementViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    val entries by viewModel.parsedEntries.collectAsState()

    val partyHandler = remember { PartyHandler() }
    val categoryHandler = remember { CategoryHandler() }
    val payers = remember { partyHandler.fetchParties(PartyType.PAYER) }
    val receivers = remember { partyHandler.fetchParties(PartyType.RECEIVER) }
    val expenseCats = remember { categoryHandler.fetchCategories(CategoryType.EXPENSE) }
    val incomeCats = remember { categoryHandler.fetchCategories(CategoryType.INCOME) }

    Column(modifier = Modifier.fillMaxSize()) {

        TextH1(text = "Revise a conciliação")
        Spacer(Modifier.height(8.dp))
        TextNormal(text = "Ajuste os dados antes de continuar.")

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(0.5.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(6.dp))
                .clip(RoundedCornerShape(6.dp))
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.onSurface.copy(alpha = 0.07f))
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.width(30.dp))
                TextH4(text = "Data", modifier = Modifier.weight(1.5f))
                TextH4(text = "Pagador/Beneficiário", modifier = Modifier.weight(2.5f))
                TextH4(text = "Descrição", modifier = Modifier.weight(2.5f))
                TextH4(text = "Valor", modifier = Modifier.weight(1.5f))
                TextH4(text = "Categoria", modifier = Modifier.weight(2f))
            }

            Divider()

            val listState = rememberLazyListState()
            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                    items(entries, key = { it.rowId }) { entry ->
                        EntryRow(
                            entry = entry,
                            payers = payers,
                            receivers = receivers,
                            expenseCats = expenseCats,
                            incomeCats = incomeCats,
                            onEntryChange = { updater -> viewModel.updateEntry(entry.rowId, updater) }
                        )
                        Divider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
                VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(listState),
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Divider(modifier = Modifier.padding(bottom = 16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TransparentButton(text = "Voltar", onClick = onBack)
            DefaultButton(
                modifier = Modifier.width(160.dp),
                text = "Importar",
                textColor = if (MaterialTheme.colors.isLight) Color.White else MaterialTheme.colors.secondary,
                onClick = onNext
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun EntryRow(
    entry: ParsedEntry,
    payers: List<Party>,
    receivers: List<Party>,
    expenseCats: List<Category>,
    incomeCats: List<Category>,
    onEntryChange: ((ParsedEntry) -> ParsedEntry) -> Unit,
) {
    val partyList = when (entry.type) {
        TransactionType.GAIN -> payers
        TransactionType.EXPENSE -> receivers
        else -> payers + receivers
    }
    val categoryList = if (entry.type == TransactionType.GAIN) incomeCats else expenseCats

    var partyExpanded by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Type indicator dot
        Box(modifier = Modifier.width(30.dp), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(
                        if (entry.type == TransactionType.GAIN) MaterialTheme.colors.onPrimary
                        else MaterialTheme.colors.onError
                    )
            )
        }

        // Date
        DateTimePicker(
            modifier = Modifier.weight(1.5f).padding(end = 8.dp),
            showLabel = false,
            value = entry.date,
            selectedDateTime = { newDt -> onEntryChange { it.copy(date = newDt) } }
        )

        // Party
        Box(modifier = Modifier.weight(2.5f).padding(end = 8.dp)) {
            DropDownTextField(
                value = entry.party?.name ?: "",
                placeholder = entry.rawCounterpartyName.ifBlank { "Selecionar" },
                onClick = { partyExpanded = true }
            )
            DropdownMenu(
                expanded = partyExpanded,
                onDismissRequest = { partyExpanded = false }
            ) {
                partyList.forEach { p ->
                    DropdownMenuItem(onClick = {
                        onEntryChange { it.copy(party = p, needsNewParty = false) }
                        partyExpanded = false
                    }) {
                        TextNormal(text = p.name)
                    }
                }
                if (partyList.isEmpty()) {
                    DropdownMenuItem(onClick = { partyExpanded = false }) {
                        TextNormal(
                            text = "Nenhum cadastrado",
                            color = MaterialTheme.colors.primary.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }

        // Description
        DefaultTextField(
            modifier = Modifier.weight(2.5f).padding(end = 8.dp),
            value = entry.description,
            onValueChange = { onEntryChange { e -> e.copy(description = it) } }
        )

        // Value
        val balanceText = remember(entry.balance) { toBrMoney.format(abs(entry.balance)) }
        DefaultTextField(
            modifier = Modifier.weight(1.5f).padding(end = 8.dp),
            value = balanceText,
            textAlign = TextAlign.End,
            onValueChange = { input ->
                val digits = input.filter { c -> c.isDigit() || c == ',' || c == '.' }
                if (digits.isNotEmpty()) {
                    val parsed = digits.replace(".", "").replace(",", ".").toDoubleOrNull() ?: return@DefaultTextField
                    val signed = if (entry.type == TransactionType.EXPENSE) -abs(parsed) else abs(parsed)
                    onEntryChange { it.copy(balance = signed) }
                }
            }
        )

        // Category
        Box(modifier = Modifier.weight(2f)) {
            DropDownTextField(
                value = entry.category?.name ?: "",
                placeholder = "—",
                icon = entry.category?.icon,
                onClick = { categoryExpanded = true }
            )
            DropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false }
            ) {
                categoryList.forEach { c ->
                    DropdownMenuItem(onClick = {
                        onEntryChange { it.copy(category = c) }
                        categoryExpanded = false
                    }) {
                        TextNormal(text = c.name)
                    }
                }
                if (categoryList.isEmpty()) {
                    DropdownMenuItem(onClick = { categoryExpanded = false }) {
                        TextNormal(
                            text = "Nenhuma cadastrada",
                            color = MaterialTheme.colors.primary.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}
