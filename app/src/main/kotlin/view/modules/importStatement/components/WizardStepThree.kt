package view.modules.importStatement.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import application.category.CategoryHandler
import application.party.PartyHandler
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Trash
import com.adamglin.phosphoricons.light.Warning
import domain.entity.Category
import domain.entity.Party
import domain.enums.CategoryType
import domain.enums.PartyType
import domain.enums.TransactionType
import domain.structs.ParsedEntry
import utils.toBrMoney
import view.shared.*
import viewModel.ImportStatementViewModel
import java.time.format.DateTimeFormatter

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
                Box(modifier = Modifier.width(32.dp))
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
                            onEntryChange = { updater -> viewModel.updateEntry(entry.rowId, updater) },
                            onRemove = { viewModel.removeEntry(entry.rowId) }
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

        val hasDuplicates = entries.any { it.isPossibleDuplicate }
        if (hasDuplicates) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = PhosphorIcons.Light.Warning,
                    contentDescription = null,
                    tint = Color(0xFFB7950B),
                    modifier = Modifier.size(15.dp)
                )
                Spacer(Modifier.width(6.dp))
                TextNormal(
                    text = "Os registros destacados em amarelo podem ser duplicações de transações já cadastradas nessa conta. Revise antes de prosseguir.",
                    color = Color(0xFFB7950B),
                )
            }
            Spacer(Modifier.height(16.dp))
        }

        Divider(modifier = Modifier.padding(bottom = 16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TransparentButton(text = "Voltar", onClick = onBack)
            DefaultButton(
                modifier = Modifier.width(200.dp),
                text = "Importar",
                textColor = if (MaterialTheme.colors.isLight) Color.White else MaterialTheme.colors.secondary,
                onClick = onNext
            )
        }
    }
}

@Composable
private fun EntryRow(
    entry: ParsedEntry,
    payers: List<Party>,
    receivers: List<Party>,
    expenseCats: List<Category>,
    incomeCats: List<Category>,
    onEntryChange: ((ParsedEntry) -> ParsedEntry) -> Unit,
    onRemove: () -> Unit,
) {
    val partyList = when (entry.type) {
        TransactionType.GAIN -> payers
        TransactionType.EXPENSE -> receivers
        else -> payers + receivers
    }
    val categoryList = if (entry.type == TransactionType.GAIN) incomeCats else expenseCats

    val isLight = MaterialTheme.colors.isLight
    val rowBackground = if (entry.isPossibleDuplicate)
        if (isLight) Color(0xFFFFF9C4) else Color(0xFF3D3500)
    else Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(rowBackground)
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
        val dateText = remember(entry.date) { entry.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) }
        DefaultTextField(
            modifier = Modifier.weight(1.5f).padding(end = 8.dp),
            value = dateText,
            enabled = false,
            showBorder = false,
            textColor = MaterialTheme.colors.primary.copy(alpha = 0.5f),
            fontStyle = FontStyle.Italic,
            onValueChange = {}
        )

        // Party
        SearchablePartyField(
            modifier = Modifier.weight(2.5f).padding(end = 8.dp),
            value = entry.party?.name ?: entry.customPartyName ?: entry.rawCounterpartyName,
            placeholder = "Selecionar",
            options = partyList,
            showBorder = false,
            borderOnActive = true,
            maxLength = 100,
            onPartySelected = { p: Party ->
                onEntryChange { it.copy(party = p, needsNewParty = false, customPartyName = null) }
            },
            onFreeText = { name: String ->
                onEntryChange { it.copy(party = null, needsNewParty = name.isNotBlank(), customPartyName = name.takeIf { name.isNotBlank() }) }
            }
        )

        // Description
        DefaultTextField(
            modifier = Modifier.weight(2.5f).padding(end = 8.dp),
            value = entry.description,
            showBorder = false,
            borderOnActive = true,
            onValueChange = { if (it.length <= 255) onEntryChange { e -> e.copy(description = it) } }
        )

        // Value
        val balanceText = remember(entry.balance) { toBrMoney.format(entry.balance) }
        DefaultTextField(
            modifier = Modifier.weight(1.5f).padding(end = 8.dp),
            value = balanceText,
            textAlign = TextAlign.Center,
            enabled = false,
            showBorder = false,
            textColor = MaterialTheme.colors.primary.copy(alpha = 0.5f),
            fontStyle = FontStyle.Italic,
            onValueChange = {}
        )

        // Category
        val categoryDisplayValue = remember(entry.category, entry.subcategory, entry.customCategoryText) {
            when {
                entry.subcategory != null -> "${entry.category!!.name}/${entry.subcategory.name}"
                entry.category != null -> entry.category.name
                else -> entry.customCategoryText.orEmpty()
            }
        }
        SearchableCategoryField(
            modifier = Modifier.weight(2f),
            value = categoryDisplayValue,
            placeholder = "—",
            options = categoryList,
            showBorder = false,
            borderOnActive = true,
            maxCategoryLength = 100,
            maxSubcategoryLength = 100,
            onCategorySelected = { c, sub ->
                onEntryChange { it.copy(category = c, subcategory = sub, customCategoryText = null) }
            },
            onFreeText = { text ->
                onEntryChange {
                    it.copy(
                        category = null,
                        subcategory = null,
                        customCategoryText = text.takeIf { t -> t.isNotBlank() }
                    )
                }
            }
        )


        // Remove button
        TooltipBox("Remover linha"){
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .pointerHoverIcon(PointerIcon.Hand)
                    .clickable { onRemove() }
            ) {
                Icon(
                    imageVector = PhosphorIcons.Light.Trash,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp),
                    tint = MaterialTheme.colors.primary
                )
            }
        }

    }
}
