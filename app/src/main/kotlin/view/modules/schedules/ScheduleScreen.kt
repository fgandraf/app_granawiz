package view.modules.schedules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.light.MinusSquare
import com.adamglin.phosphoricons.light.PlusSquare
import com.adamglin.phosphoricons.regular.Calendar
import com.adamglin.phosphoricons.regular.Pencil
import com.felipegandra.generated.resources.*
import application.schedule.usecases.ScheduleOccurrence
import domain.entity.Schedule
import domain.enums.TransactionType
import domain.structs.PageAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import view.modules.schedules.component.DropDownAddSchedule
import view.modules.schedules.component.ScheduleGroupHeader
import view.modules.schedules.component.ScheduleRow
import view.modules.transactionForm.TransactionForm
import view.shared.*
import viewModel.ScheduleViewModel
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.time.LocalDate
import java.time.LocalTime


@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = remember { ScheduleViewModel() },
    onSidebarReload: () -> Unit = {},
) {
    val initialAddress = listOf(
        PageAddress(
            iconVector = PhosphorIcons.Regular.Calendar,
            iconSize = DpSize(21.dp, 18.dp),
            name = stringResource(Res.string.nav_schedules),
            rootPath = true
        )
    )

    var selectedSchedule by remember { mutableStateOf<Schedule?>(null) }
    var selectedOccurrenceIndex by remember { mutableStateOf(0) }
    var showForm by remember { mutableStateOf(false) }
    var addresses by remember { mutableStateOf(initialAddress) }
    var backIcon by remember { mutableStateOf(false) }
    var newType by remember { mutableStateOf<TransactionType?>(null) }

    val strScheduleEditTitle = stringResource(Res.string.schedule_edit_title)
    val strScheduleNewIncome = stringResource(Res.string.schedule_new_income)
    val strScheduleNewExpense = stringResource(Res.string.schedule_new_expense)

    val schedulesState by viewModel.schedules.collectAsState()
    val groupsState by viewModel.groups.collectAsState()
    val filters by viewModel.filters.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 15.dp, bottom = 15.dp)
            .border(1.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colors.surface)
    ) {
        DefaultScreenHeader(
            addresses = addresses,
            backEnabled = backIcon,
            onBackClick = {
                addresses = initialAddress
                selectedSchedule = null
                showForm = false
                backIcon = false
            },
            trailingContent = if (!showForm && schedulesState.isNotEmpty()) {
                { SearchField(value = filters.searchQuery, onValueChange = { viewModel.updateFilters { copy(searchQuery = it) } }) }
            } else null
        )

        if (!showForm) {
            Body(
                viewModel = viewModel,
                onScheduleEdit = { occ ->
                    selectedSchedule = occ.schedule
                    selectedOccurrenceIndex = occ.index
                    showForm = true
                    newType = null
                    backIcon = true
                    addresses = initialAddress + PageAddress(
                        iconVector = PhosphorIcons.Regular.Pencil,
                        iconSize = DpSize(21.dp, 18.dp),
                        name = strScheduleEditTitle
                    )
                },
                onSidebarReload = onSidebarReload,
                onAddGain = {
                    newType = TransactionType.GAIN
                    selectedSchedule = null
                    showForm = true
                    backIcon = true
                    addresses = initialAddress + PageAddress(
                        iconVector = PhosphorIcons.Light.PlusSquare,
                        iconSize = DpSize(21.dp, 18.dp),
                        name = strScheduleNewIncome
                    )
                },
                onAddExpense = {
                    newType = TransactionType.EXPENSE
                    selectedSchedule = null
                    showForm = true
                    backIcon = true
                    addresses = initialAddress + PageAddress(
                        iconVector = PhosphorIcons.Light.MinusSquare,
                        iconSize = DpSize(21.dp, 18.dp),
                        name = strScheduleNewExpense
                    )
                },
            )
        }

        if (showForm) {
            TransactionForm(
                allAccounts = groupsState.flatMap { it.accounts },
                schedule = selectedSchedule,
                occurrenceIndex = if (selectedSchedule != null) selectedOccurrenceIndex else null,
                transactionType = newType,
                initialAccount = filters.account,
                lockAccount = false,
                onDismiss = { saved ->
                    showForm = false
                    selectedSchedule = null
                    selectedOccurrenceIndex = 0
                    newType = null
                    backIcon = false
                    addresses = initialAddress
                    if (saved) viewModel.getSchedules()
                }
            )
        }
    }
}

@Composable
private fun Body(
    viewModel: ScheduleViewModel,
    onScheduleEdit: (ScheduleOccurrence) -> Unit,
    onSidebarReload: () -> Unit,
    onAddGain: () -> Unit,
    onAddExpense: () -> Unit,
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val strExportDialogTitle = stringResource(Res.string.schedule_export_dialog_title)
    var showAddDropDown by remember { mutableStateOf(false) }

    val schedulesState by viewModel.schedules.collectAsState()
    val paidTransactionsState by viewModel.paidTransactions.collectAsState()
    val filters by viewModel.filters.collectAsState()

    val today = remember { LocalDate.now() }
    val windowStart = remember { today.minusMonths(3).atStartOfDay() }
    val windowEnd = remember { today.plusMonths(12).atTime(LocalTime.MAX) }

    val occurrences = remember(schedulesState, paidTransactionsState) {
        viewModel.buildOccurrences(windowStart, windowEnd)
    }

    val filtered = remember(occurrences, filters) {
        occurrences.filter { occ ->
            val matchesSearch = filters.searchQuery.isEmpty() ||
                    occ.schedule.party.name.contains(filters.searchQuery, ignoreCase = true) ||
                    occ.schedule.description.contains(filters.searchQuery, ignoreCase = true) ||
                    occ.schedule.category.name.contains(filters.searchQuery, ignoreCase = true) ||
                    occ.schedule.subcategory?.name?.contains(filters.searchQuery, ignoreCase = true) == true ||
                    occ.schedule.tags?.any { it.name.contains(filters.searchQuery, ignoreCase = true) } == true
            val matchesAccount = filters.account == null || occ.schedule.account.id == filters.account!!.id
            val matchesType = filters.type == null || occ.schedule.type == filters.type
            val matchesCategory = filters.categoryItem == null ||
                    (filters.categoryItem!!.second == null && occ.schedule.category.id == filters.categoryItem!!.first.id) ||
                    (filters.categoryItem!!.second != null && occ.schedule.subcategory?.id == filters.categoryItem!!.second!!.id)
            val tag = filters.tag
            val matchesTag = tag == null || occ.schedule.tags?.any { it.id == tag.id } == true
            matchesSearch && matchesAccount && matchesType && matchesCategory && matchesTag
        }
    }

    val startOfToday = today.atStartOfDay()
    val endOfToday = today.atTime(LocalTime.MAX)
    val endOfMonth = today.withDayOfMonth(today.lengthOfMonth()).atTime(LocalTime.MAX)

    val groups = listOf(
        stringResource(Res.string.schedules_group_overdue) to filtered.filter { it.dueDate.isBefore(startOfToday) },
        stringResource(Res.string.schedules_group_due_today) to filtered.filter { !it.dueDate.isBefore(startOfToday) && !it.dueDate.isAfter(endOfToday) },
        stringResource(Res.string.schedules_group_due_this_month) to filtered.filter { it.dueDate.isAfter(endOfToday) && !it.dueDate.isAfter(endOfMonth) },
        stringResource(Res.string.schedules_group_upcoming) to filtered.filter { it.dueDate.isAfter(endOfMonth) },
    ).filter { it.second.isNotEmpty() }

    Row(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (groups.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        TextH2(text = stringResource(Res.string.schedules_empty))
                    }
                } else {
                    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                        item { Spacer(Modifier.height(30.dp)) }
                        groups.forEach { (title, items) ->
                            item {
                                GroupSection(
                                    title = title,
                                    items = items,
                                    today = today,
                                    onScheduleEdit = onScheduleEdit,
                                    onMarkAsPaid = { occ ->
                                        viewModel.markAsPaid(occ)
                                        onSidebarReload()
                                    },
                                    onDeleteThisOccurrence = { occ -> viewModel.deleteThisOccurrence(occ) },
                                    onDeleteThisAndFuture = { occ -> viewModel.deleteThisAndFuture(occ) },
                                )
                            }
                        }
                        item { Spacer(Modifier.height(50.dp)) }
                    }
                }

                CircleButton(onClick = { showAddDropDown = true }) {
                    if (showAddDropDown) {
                        DropDownAddSchedule(
                            expanded = showAddDropDown,
                            onClickGain = { showAddDropDown = false; onAddGain() },
                            onClickExpense = { showAddDropDown = false; onAddExpense() },
                            onDismissRequest = { showAddDropDown = false }
                        )
                    }
                }
            }
        }

        FilterTransactionBar(
            listState = listState,
            visible = schedulesState.isNotEmpty(),
            items = viewModel.schedules,
            searchQuery = filters.searchQuery,
            filterAccount = filters.account,
            onFilterAccountChange = { viewModel.updateFilters { copy(account = it) } },
            filterType = filters.type,
            onFilterTypeChange = { viewModel.updateFilters { copy(type = it) } },
            filterCategoryItem = filters.categoryItem,
            onFilterCategoryItemChange = { viewModel.updateFilters { copy(categoryItem = it) } },
            filterTag = filters.tag,
            onFilterTagChange = { viewModel.updateFilters { copy(tag = it) } },
            groups = viewModel.groups,
            onClearFilters = { viewModel.clearFilters() },
            onExport = {
                val dialog = FileDialog(null as Frame?, strExportDialogTitle, FileDialog.SAVE)
                dialog.file = "agendamentos_${LocalDate.now()}.xlsx"
                dialog.isVisible = true
                val dir = dialog.directory
                val name = dialog.file
                dialog.dispose()
                if (dir != null && name != null) {
                    val safeName = if (name.endsWith(".xlsx")) name else "$name.xlsx"
                    val target = File(dir, safeName)
                    scope.launch(Dispatchers.IO) {
                        viewModel.exportToExcel(filtered, target)
                    }
                }
            },
            exportLabel = Res.string.filter_export_to_excel
        )
    }
}

@Composable
private fun GroupSection(
    title: String,
    items: List<ScheduleOccurrence>,
    today: LocalDate,
    onScheduleEdit: (ScheduleOccurrence) -> Unit,
    onMarkAsPaid: (ScheduleOccurrence) -> Unit,
    onDeleteThisOccurrence: (ScheduleOccurrence) -> Unit,
    onDeleteThisAndFuture: (ScheduleOccurrence) -> Unit,
) {
    ScheduleGroupHeader(modifier = Modifier.zIndex(1f), title = title)
    Column(
        modifier = Modifier
            .padding(start = 80.dp, end = 30.dp)
            .zIndex(2f)
            .clip(RoundedCornerShape(topEnd = 0.dp, bottomStart = 0.dp))
            .background(
                MaterialTheme.colors.background.copy(0.6f),
                RoundedCornerShape(topEnd = 0.dp, bottomStart = 0.dp)
            )
            .border(
                0.5.dp,
                MaterialTheme.colors.onSurface,
                RoundedCornerShape(topEnd = 0.dp, bottomStart = 0.dp)
            )
    ) {
        Spacer(Modifier.height(20.dp))
        items.forEach { occ ->
            ScheduleRow(
                occurrence = occ,
                overdue = occ.dueDate.isBefore(today.atStartOfDay()),
                onEdit = { onScheduleEdit(occ) },
                onMarkAsPaid = { onMarkAsPaid(occ) },
                onDeleteThisOccurrence = { onDeleteThisOccurrence(occ) },
                onDeleteThisAndFuture = { onDeleteThisAndFuture(occ) },
            )
        }
        Spacer(Modifier.height(20.dp))
    }
    Spacer(Modifier.height(30.dp))
}
