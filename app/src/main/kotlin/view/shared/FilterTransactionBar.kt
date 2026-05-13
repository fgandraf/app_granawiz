package view.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.*
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import domain.contracts.IFilterable
import domain.entity.Category
import domain.entity.Group
import domain.entity.Subcategory
import domain.entity.Tag
import domain.entity.account.BankAccount
import domain.enums.TransactionType
import domain.structs.FilterEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun FilterTransactionBar(
    items: StateFlow<List<IFilterable>>,
    searchQuery: String,
    currentAccountView: BankAccount? = null,
    filterAccount: BankAccount? = null,
    onFilterAccountChange: (BankAccount?) -> Unit,
    filterType: TransactionType? = null,
    onFilterTypeChange: (TransactionType?) -> Unit,
    filterCategoryItem: Pair<Category, Subcategory?>? = null,
    onFilterCategoryItemChange: (Pair<Category, Subcategory?>?) -> Unit,
    filterTag: Tag? = null,
    onFilterTagChange: (Tag?) -> Unit,
    filterYear: Int? = java.time.LocalDate.now().year,
    onFilterYearChange: (Int?) -> Unit = {},
    availableYears: List<Int> = emptyList(),
    groups: MutableStateFlow<List<Group>>,
    onExportExcel: () -> Unit = {},
    onClearFilters: () -> Unit = {},
) {
    val list by items.collectAsState(initial = emptyList())
    val entries: List<FilterEntry> = list.map { it.toFilterEntry() }

    val hasActiveFilters = filterAccount != null || filterType != null || filterCategoryItem != null || filterTag != null ||
            filterYear == null || filterYear != java.time.LocalDate.now().year

    val preFiltered = remember(entries, searchQuery, filterAccount) {
        entries.filter { entry ->
            val ms = searchQuery.isEmpty() ||
                    entry.partyName.contains(searchQuery, ignoreCase = true) ||
                    entry.description.contains(searchQuery, ignoreCase = true) ||
                    entry.category.name.contains(searchQuery, ignoreCase = true) ||
                    entry.subcategory?.name?.contains(searchQuery, ignoreCase = true) == true ||
                    entry.tags?.any { it.name.contains(searchQuery, ignoreCase = true) } == true
            val ma = filterAccount == null || entry.accountId == filterAccount.id
            ms && ma
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        ClearFiltersButton(
            hasActiveFilters = hasActiveFilters,
            onClearFilters = onClearFilters
        )

        Divider(Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colors.background))

        YearDropDown(
            availableYears = availableYears,
            onFilterYearChange = onFilterYearChange
        )

        AccountDropDown(
            currentAccountView = currentAccountView,
            groups = groups,
            onFilterAccountChange = onFilterAccountChange
        )

        TypeDropDown(
            onFilterTypeChange = onFilterTypeChange
        )

        CategoriesDropDown(
            filterType = filterType,
            onFilterCategoryItemChange = onFilterCategoryItemChange,
            preFiltered = preFiltered
        )

        TagsDropDown(
            preFiltered = preFiltered,
            filterType = filterType,
            filterCategoryItem = filterCategoryItem,
            onFilterTagChange = onFilterTagChange
        )

        Divider(Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colors.background))

        ExportDropDown(onExportExcel = onExportExcel)

    }
}


@Composable
private fun YearDropDown(
    availableYears: List<Int>,
    onFilterYearChange: (Int?) -> Unit
) {
    var showYearDropdown by remember { mutableStateOf(false) }

    TooltipBox(stringResource(Res.string.filter_year)) {
        Box(
            modifier = Modifier
                .height(30.dp)
                .background(Color.Transparent)
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable { showYearDropdown = true }
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                Icon(
                    imageVector = PhosphorIcons.Light.Calendar,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(20.dp)
                )
                Icon(
                    imageVector = PhosphorIcons.Light.CaretDown,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(8.dp)
                )
            }
        }
        DropdownMenu(
            expanded = showYearDropdown,
            onDismissRequest = { showYearDropdown = false }
        ) {
            DropdownMenuItem(onClick = {
                onFilterYearChange(null)
                showYearDropdown = false
            }) {
                TextNormal(text = stringResource(Res.string.filter_all_years))
            }
            availableYears.forEach { year ->
                DropdownMenuItem(onClick = {
                    onFilterYearChange(year)
                    showYearDropdown = false
                }) {
                    TextNormal(text = year.toString())
                }
            }
        }
    }
}

@Composable
private fun AccountDropDown(
    currentAccountView: BankAccount? = null,
    groups: MutableStateFlow<List<Group>>,
    onFilterAccountChange: (BankAccount?) -> Unit
){
    if (currentAccountView != null)
        return

    var showAccountDropdown by remember { mutableStateOf(false) }
    val groupsList by groups.collectAsState(initial = emptyList())
    val allAccounts = groupsList.flatMap { it.accounts }

    TooltipBox(stringResource(Res.string.filter_account)){
        Box(
            modifier = Modifier
                .height(30.dp)
                .background(Color.Transparent)
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable { showAccountDropdown = true }
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                Icon(
                    imageVector = PhosphorIcons.Light.Bank,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(20.dp)
                )
                Icon(
                    imageVector = PhosphorIcons.Light.CaretDown,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(8.dp)
                )
            }
        }
        DropdownMenu(
            expanded = showAccountDropdown,
            onDismissRequest = { showAccountDropdown = false }
        ) {
            DropdownMenuItem(onClick = {
                onFilterAccountChange(null)
                showAccountDropdown = false
            }) {
                TextNormal(text = stringResource(Res.string.filter_all_accounts))
            }
            allAccounts.forEach { acc: BankAccount ->
                DropdownMenuItem(onClick = {
                    onFilterAccountChange(acc)
                    showAccountDropdown = false
                }) {
                    TextNormal(text = acc.name)
                }
            }
        }
    }

}

@Composable
private fun TypeDropDown(
    onFilterTypeChange: (TransactionType?) -> Unit
){
    var showTypeDropdown by remember { mutableStateOf(false) }
    TooltipBox(stringResource(Res.string.filter_type)){
        Box(
            modifier = Modifier
                .height(30.dp)
                .background(Color.Transparent)
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable { showTypeDropdown = true }
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                Icon(
                    imageVector = PhosphorIcons.Light.ArrowsDownUp,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(20.dp)
                )
                Icon(
                    imageVector = PhosphorIcons.Light.CaretDown,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(8.dp)
                )
            }
        }
        DropdownMenu(
            expanded = showTypeDropdown,
            onDismissRequest = { showTypeDropdown = false }
        ) {
            DropdownMenuItem(onClick = {
                onFilterTypeChange(null)
                showTypeDropdown = false
            }) {
                TextNormal(text = stringResource(Res.string.filter_all_types))
            }
            DropdownMenuItem(onClick = {
                onFilterTypeChange(TransactionType.GAIN)
                showTypeDropdown = false
            }) {
                TextNormal(text = stringResource(Res.string.filter_incomes))
            }
            DropdownMenuItem(onClick = {
                onFilterTypeChange(TransactionType.EXPENSE)
                showTypeDropdown = false
            }) {
                TextNormal(text = stringResource(Res.string.filter_expenses))
            }
        }
    }
}

@Composable
private fun CategoriesDropDown(
    filterType: TransactionType? = null,
    onFilterCategoryItemChange: (Pair<Category, Subcategory?>?) -> Unit,
    preFiltered: List<FilterEntry>
){
    var showCategoryDropdown by remember { mutableStateOf(false) }
    val availableCategoriesMap = remember(preFiltered, filterType) {
        preFiltered
            .filter { filterType == null || it.type == filterType }
            .groupBy { it.category }
    }

    TooltipBox(stringResource(Res.string.filter_category)){
        Box(
            modifier = Modifier
                .height(30.dp)
                .background(Color.Transparent)
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable { showCategoryDropdown = true }
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                Icon(
                    imageVector = PhosphorIcons.Light.Shapes,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(20.dp)
                )
                Icon(
                    imageVector = PhosphorIcons.Light.CaretDown,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(8.dp)
                )
            }
        }
        DropdownMenu(
            expanded = showCategoryDropdown,
            onDismissRequest = { showCategoryDropdown = false }
        ) {
            DropdownMenuItem(onClick = {
                onFilterCategoryItemChange(null)
                showCategoryDropdown = false
            }) {
                TextNormal(text = stringResource(Res.string.filter_all_categories))
            }
            availableCategoriesMap.forEach { (cat, entries) ->
                DropdownMenuItem(onClick = {
                    onFilterCategoryItemChange(Pair(cat, null))
                    showCategoryDropdown = false
                }) {
                    TextNormal(text = cat.name)
                }
                entries.mapNotNull { it.subcategory }.distinctBy { it.id }.forEach { sub ->
                    DropdownMenuItem(onClick = {
                        onFilterCategoryItemChange(Pair(cat, sub))
                        showCategoryDropdown = false
                    }) {
                        TextNormal(modifier = Modifier.padding(start = 16.dp), text = sub.name)
                    }
                }
            }
        }
    }
}

@Composable
private fun TagsDropDown(
    preFiltered: List<FilterEntry>,
    filterType: TransactionType? = null,
    filterCategoryItem: Pair<Category, Subcategory?>? = null,
    onFilterTagChange: (Tag?) -> Unit
){
    var showTagDropdown by remember { mutableStateOf(false) }
    val availableTags = remember(preFiltered, filterType, filterCategoryItem) {
        preFiltered.filter { entry ->
            val mt = filterType == null || entry.type == filterType
            val mc = filterCategoryItem == null ||
                    (filterCategoryItem.second == null && entry.category.id == filterCategoryItem.first.id) ||
                    (filterCategoryItem.second != null && entry.subcategory?.id == filterCategoryItem.second!!.id)
            mt && mc
        }.flatMap { it.tags ?: emptyList() }.distinctBy { it.id }
    }

    TooltipBox(stringResource(Res.string.filter_tag)){
        Box(
            modifier = Modifier
                .height(30.dp)
                .background(Color.Transparent)
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable { showTagDropdown = true }
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                Icon(
                    imageVector = PhosphorIcons.Light.Tag,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(20.dp)
                )
                Icon(
                    imageVector = PhosphorIcons.Light.CaretDown,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(8.dp)
                )
            }
        }
        DropdownMenu(
            expanded = showTagDropdown,
            onDismissRequest = { showTagDropdown = false }
        ) {
            DropdownMenuItem(onClick = {
                onFilterTagChange(null)
                showTagDropdown = false
            }) {
                TextNormal(text = stringResource(Res.string.filter_all_tags))
            }
            availableTags.forEach { tag ->
                DropdownMenuItem(onClick = {
                    onFilterTagChange(tag)
                    showTagDropdown = false
                }) {
                    TextNormal(text = tag.name)
                }
            }
        }
    }

}


@Composable
private fun ClearFiltersButton(
    hasActiveFilters: Boolean,
    onClearFilters: () -> Unit
) {
    val tint = if (hasActiveFilters) MaterialTheme.colors.secondary else MaterialTheme.colors.secondary.copy(alpha = 0.3f)

    TooltipBox(if (hasActiveFilters) stringResource(Res.string.filter_clear) else "") {
        Box(
            modifier = Modifier
                .height(30.dp)
                .fillMaxWidth()
                .background(Color.Transparent)
                .pointerHoverIcon(if (hasActiveFilters) PointerIcon.Hand else PointerIcon.Default)
                .clickable(enabled = hasActiveFilters) { onClearFilters() }
                .padding(end = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = PhosphorIcons.Light.FunnelX,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ExportDropDown(onExportExcel: () -> Unit = {}){
    var showExportDropdown by remember { mutableStateOf(false) }

    TooltipBox(stringResource(Res.string.filter_export)){
        Box(
            modifier = Modifier
                .height(30.dp)
                .background(Color.Transparent)
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable { showExportDropdown = true }
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                Icon(
                    imageVector = PhosphorIcons.Light.Export,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(20.dp)
                )
                Icon(
                    imageVector = PhosphorIcons.Light.CaretDown,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(8.dp)
                )
            }
        }
        DropdownMenu(
            expanded = showExportDropdown,
            onDismissRequest = { showExportDropdown = false }
        ) {
            DropdownMenuItem(
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                onClick = {
                showExportDropdown = false
                onExportExcel()
            }) {
                Icon(
                    imageVector = PhosphorIcons.Light.Table,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(5.dp))
                TextNormal(text = stringResource(Res.string.filter_export_to_excel))
            }
        }
    }
}