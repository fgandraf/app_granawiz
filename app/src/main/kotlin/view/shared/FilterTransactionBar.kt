package view.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.*
import core.contracts.IFilterable
import core.entity.*
import core.entity.account.BankAccount
import core.enums.TransactionType
import core.structs.FilterEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import view.theme.Ubuntu
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun FilterTransactionBar(
    items: StateFlow<List<IFilterable>>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    currentAccountView: BankAccount? = null,
    filterAccount: BankAccount? = null,
    onFilterAccountChange: (BankAccount?) -> Unit,
    filterType: TransactionType? = null,
    onFilterTypeChange: (TransactionType?) -> Unit,
    filterCategoryItem: Pair<Category, Subcategory?>? = null,
    onFilterCategoryItemChange: (Pair<Category, Subcategory?>?) -> Unit,
    filterTag: Tag? = null,
    onFilterTagChange: (Tag?) -> Unit,
    groups: MutableStateFlow<List<Group>>
) {
    val list by items.collectAsState(initial = emptyList())
    val entries: List<FilterEntry> = list.map { it.toFilterEntry() }

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

    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 2.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
    ) {
        AccountDropDown(
            currentAccountView = currentAccountView,
            groups = groups,
            filterAccount = filterAccount,
            onFilterAccountChange = onFilterAccountChange
        )

        TypeDropDown(
            filterType = filterType,
            onFilterTypeChange = onFilterTypeChange
        )

        CategoriesDropDown(
            filterCategoryItem = filterCategoryItem,
            filterType = filterType,
            onFilterCategoryItemChange = onFilterCategoryItemChange,
            preFiltered = preFiltered
        )

        TagsDropDown(
            filterTag = filterTag,
            preFiltered = preFiltered,
            filterType = filterType,
            filterCategoryItem = filterCategoryItem,
            onFilterTagChange = onFilterTagChange
        )

        SearchField(
            value = searchQuery,
            onValueChange = onSearchQueryChange
        )
    }
}


@Composable
private fun AccountDropDown(
    currentAccountView: BankAccount? = null,
    groups: MutableStateFlow<List<Group>>,
    filterAccount: BankAccount? = null,
    onFilterAccountChange: (BankAccount?) -> Unit
){
    if (currentAccountView != null)
        return

    var showAccountDropdown by remember { mutableStateOf(false) }
    val groupsList by groups.collectAsState(initial = emptyList())
    val allAccounts = groupsList.flatMap { it.accounts }
    val filterLabel = filterAccount?.name ?: "Todas as contas"

    Box {
        Box(
            modifier = Modifier
                .height(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(8.dp))
                .background(Color.Transparent)
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable { showAccountDropdown = true }
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(
                    imageVector = PhosphorIcons.Light.Bank,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(16.dp)
                )
                TextNormal(text = filterLabel)
                Icon(
                    imageVector = PhosphorIcons.Light.CaretDown,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(14.dp)
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
                TextNormal(text = "Todas as contas")
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
    filterType: TransactionType? = null,
    onFilterTypeChange: (TransactionType?) -> Unit
){
    var showTypeDropdown by remember { mutableStateOf(false) }
    val typeFilterLabel = when (filterType) {
        TransactionType.GAIN -> "Receitas"
        TransactionType.EXPENSE -> "Despesas"
        else -> "Todos os tipos"
    }
    Box {
        Box(
            modifier = Modifier
                .height(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(8.dp))
                .background(Color.Transparent)
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable { showTypeDropdown = true }
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(
                    imageVector = PhosphorIcons.Light.ArrowsDownUp,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(16.dp)
                )
                TextNormal(text = typeFilterLabel)
                Icon(
                    imageVector = PhosphorIcons.Light.CaretDown,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(14.dp)
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
                TextNormal(text = "Todos os tipos")
            }
            DropdownMenuItem(onClick = {
                onFilterTypeChange(TransactionType.GAIN)
                showTypeDropdown = false
            }) {
                TextNormal(text = "Receitas")
            }
            DropdownMenuItem(onClick = {
                onFilterTypeChange(TransactionType.EXPENSE)
                showTypeDropdown = false
            }) {
                TextNormal(text = "Despesas")
            }
        }
    }
}

@Composable
private fun CategoriesDropDown(
    filterCategoryItem: Pair<Category, Subcategory?>? = null,
    filterType: TransactionType? = null,
    onFilterCategoryItemChange: (Pair<Category, Subcategory?>?) -> Unit,
    preFiltered: List<FilterEntry>
){
    var showCategoryDropdown by remember { mutableStateOf(false) }
    val categoryFilterLabel = filterCategoryItem?.let { (cat, sub) ->
        if (sub != null) "${cat.name}: ${sub.name}" else cat.name
    } ?: "Todas as categorias"
    val availableCategoriesMap = remember(preFiltered, filterType) {
        preFiltered
            .filter { filterType == null || it.type == filterType }
            .groupBy { it.category }
    }

    Box {
        Box(
            modifier = Modifier
                .height(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(8.dp))
                .background(Color.Transparent)
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable { showCategoryDropdown = true }
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(
                    imageVector = PhosphorIcons.Light.Shapes,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(16.dp)
                )
                TextNormal(text = categoryFilterLabel)
                Icon(
                    imageVector = PhosphorIcons.Light.CaretDown,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(14.dp)
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
                TextNormal(text = "Todas as categorias")
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
    filterTag: Tag? = null,
    preFiltered: List<FilterEntry>,
    filterType: TransactionType? = null,
    filterCategoryItem: Pair<Category, Subcategory?>? = null,
    onFilterTagChange: (Tag?) -> Unit
){
    var showTagDropdown by remember { mutableStateOf(false) }
    val tagFilterLabel = filterTag?.name ?: "Todas as tags"
    val availableTags = remember(preFiltered, filterType, filterCategoryItem) {
        preFiltered.filter { entry ->
            val mt = filterType == null || entry.type == filterType
            val mc = filterCategoryItem == null ||
                    (filterCategoryItem.second == null && entry.category.id == filterCategoryItem.first.id) ||
                    (filterCategoryItem.second != null && entry.subcategory?.id == filterCategoryItem.second!!.id)
            mt && mc
        }.flatMap { it.tags ?: emptyList() }.distinctBy { it.id }
    }

    Box {
        Box(
            modifier = Modifier
                .height(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(8.dp))
                .background(Color.Transparent)
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable { showTagDropdown = true }
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(
                    imageVector = PhosphorIcons.Light.Tag,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(16.dp)
                )
                TextNormal(text = tagFilterLabel)
                Icon(
                    imageVector = PhosphorIcons.Light.CaretDown,
                    contentDescription = "",
                    tint = MaterialTheme.colors.secondary,
                    modifier = Modifier.size(14.dp)
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
                TextNormal(text = "Todas as tags")
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
private fun SearchField(
    value: String,
    onValueChange: (String) -> Unit
) {

    var textFieldValue by remember { mutableStateOf(TextFieldValue(value)) }
    LaunchedEffect(value) {
        if (textFieldValue.text != value) {
            textFieldValue = TextFieldValue(value)
        }
    }

    Row(
        modifier = Modifier
            .height(30.dp)
            .width(320.dp)
            .border(1.dp, MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(8.dp))
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                modifier = Modifier.size(40.dp).padding(7.dp).align(Alignment.Center),
                imageVector = PhosphorIcons.Light.MagnifyingGlass,
                contentDescription = "",
                tint = MaterialTheme.colors.secondary,
            )
        }

        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = textFieldValue,
            onValueChange = { textFieldValue = it; onValueChange(it.text) },
            textStyle = TextStyle(
                color = MaterialTheme.colors.secondary,
                fontSize = 14.sp,
                fontFamily = Ubuntu,
                fontWeight = FontWeight.Medium
            ),
            decorationBox = { innerTextField ->
                if (textFieldValue.text.isEmpty()) {
                    Text(
                        text = "Pesquisar",
                        color = Color.Gray.copy(alpha = 0.5f),
                        fontSize = 14.sp,
                        lineHeight = 0.sp,
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Normal,
                    )
                }
                innerTextField()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {}),
            singleLine = true
        )


    }
}
