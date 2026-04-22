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
import core.entity.*
import core.entity.account.BankAccount
import core.enums.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import view.theme.Ubuntu
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun FilterTransactionBar(
    transactions: MutableStateFlow<List<Transaction>>,
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
    val transactionsList by transactions.collectAsState(initial = emptyList())

    val preFilteredTransactions = remember(transactionsList, searchQuery, filterAccount) {
        transactionsList.filter { transaction ->
            val ms = searchQuery.isEmpty() ||
                    transaction.party.name.contains(searchQuery, ignoreCase = true) ||
                    transaction.description.contains(searchQuery, ignoreCase = true) ||
                    transaction.category.name.contains(searchQuery, ignoreCase = true) ||
                    transaction.subcategory?.name?.contains(searchQuery, ignoreCase = true) == true ||
                    transaction.tags?.any { it.name.contains(searchQuery, ignoreCase = true) } == true
            val ma = filterAccount == null || transaction.account.id == filterAccount.id
            ms && ma
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 2.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
    ) {

        // 1. Contas
        AccountDropDown(
            currentAccountView = currentAccountView,
            groups = groups,
            filterAccount = filterAccount,
            onFilterAccountChange = onFilterAccountChange
        )

        // 2. Tipo
        TypeDropDown(
            filterType = filterType,
            onFilterTypeChange = onFilterTypeChange
        )

        // 3. Categorias
        CategoriesDropDown(
            filterCategoryItem = filterCategoryItem,
            filterType = filterType,
            onFilterCategoryItemChange = onFilterCategoryItemChange,
            preFilteredTransactions = preFilteredTransactions
        )

        // 4. Etiquetas
        TagsDropDown(
            filterTag = filterTag,
            preFilteredTransactions = preFilteredTransactions,
            filterType = filterType,
            filterCategoryItem = filterCategoryItem,
            onFilterTagChange = onFilterTagChange
        )

        // 5. Pesquisa
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
    preFilteredTransactions: List<Transaction>
){
    var showCategoryDropdown by remember { mutableStateOf(false) }
    val categoryFilterLabel = filterCategoryItem?.let { (cat, sub) ->
        if (sub != null) "${cat.name}: ${sub.name}" else cat.name
    } ?: "Todas as categorias"
    val availableCategoriesMap = remember(preFilteredTransactions, filterType) {
        preFilteredTransactions
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
            availableCategoriesMap.forEach { (cat, categoryTransactions) ->
                DropdownMenuItem(onClick = {
                    onFilterCategoryItemChange(Pair(cat, null))
                    showCategoryDropdown = false
                }) {
                    TextNormal(text = cat.name)
                }
                categoryTransactions.mapNotNull { it.subcategory }.distinctBy { it.id }.forEach { sub ->
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
    preFilteredTransactions: List<Transaction>,
    filterType: TransactionType? = null,
    filterCategoryItem: Pair<Category, Subcategory?>? = null,
    onFilterTagChange: (Tag?) -> Unit
){
    var showTagDropdown by remember { mutableStateOf(false) }
    val tagFilterLabel = filterTag?.name ?: "Todas as tags"
    val availableTags = remember(preFilteredTransactions, filterType, filterCategoryItem) {
        preFilteredTransactions.filter { t ->
            val mt = filterType == null || t.type == filterType
            val mc = filterCategoryItem == null ||
                    (filterCategoryItem.second == null && t.category.id == filterCategoryItem.first.id) ||
                    (filterCategoryItem.second != null && t.subcategory?.id == filterCategoryItem.second!!.id)
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