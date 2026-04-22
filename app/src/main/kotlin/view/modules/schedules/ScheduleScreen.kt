package view.modules.schedules

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.bold.ArrowLeft
import com.adamglin.phosphoricons.light.*
import com.adamglin.phosphoricons.regular.Calendar
import com.adamglin.phosphoricons.regular.Pencil
import core.entity.Category
import core.entity.Schedule
import core.entity.Subcategory
import core.entity.account.BankAccount
import core.enums.TransactionType
import core.structs.PageAddress
import view.modules.schedules.component.DropDownAddSchedule
import view.modules.schedules.component.ScheduleGroupHeader
import view.modules.schedules.component.ScheduleRow
import view.shared.*
import view.theme.ButtonPurple
import viewModel.ScheduleViewModel
import java.time.LocalDate
import java.time.LocalTime
import core.entity.Tag as TagEntity


@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = remember { ScheduleViewModel() },
) {

    val initialAddress = listOf(
        PageAddress(
            iconVector = PhosphorIcons.Regular.Calendar,
            iconSize = DpSize(21.dp, 18.dp),
            name = "Agendamentos",
            rootPath = true
        )
    )

    var selectedSchedule by remember { mutableStateOf<Schedule?>(null) }
    var showForm by remember { mutableStateOf(false) }
    var addresses by remember { mutableStateOf(initialAddress) }
    var backIcon by remember { mutableStateOf(false) }
    var newType by remember { mutableStateOf<TransactionType?>(null) }

    var searchQuery by remember { mutableStateOf("") }
    var filterAccount by remember { mutableStateOf<BankAccount?>(null) }
    var filterType by remember { mutableStateOf<TransactionType?>(null) }
    var filterCategoryItem by remember { mutableStateOf<Pair<Category, Subcategory?>?>(null) }
    var filterTag by remember { mutableStateOf<TagEntity?>(null) }

    // Occurrences window
    val today = remember { LocalDate.now() }
    val windowStart = remember { today.minusMonths(3).atStartOfDay() }
    val windowEnd = remember { today.plusMonths(12).atTime(LocalTime.MAX) }

    val schedulesState by viewModel.schedules.collectAsState()
    val paidTransactionsState by viewModel.paidTransactions.collectAsState()

    val occurrences = remember(schedulesState, paidTransactionsState) {
        viewModel.buildOccurrences(windowStart, windowEnd)
    }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {

        // ========== HEADER ==========
        Column(modifier = Modifier.fillMaxWidth().padding(start = 20.dp, top = 20.dp, end = 20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ClickableIcon(
                    enabled = backIcon,
                    icon = PhosphorIcons.Bold.ArrowLeft,
                    iconSize = 22.dp,
                    boxSize = 25.dp
                ) {
                    addresses = initialAddress
                    selectedSchedule = null
                    showForm = false
                    backIcon = false
                }
                Spacer(Modifier.width(10.dp))
                addresses.forEach {
                    AddressView(
                        icon = it.iconVector,
                        iconSize = it.iconSize!!,
                        value = it.name,
                        rootPath = it.rootPath
                    )
                }
            }

            if (!showForm) {
                FilterTransactionBar(
                    items = viewModel.schedules,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    filterAccount = filterAccount,
                    onFilterAccountChange = { filterAccount = it },
                    filterType = filterType,
                    onFilterTypeChange = { filterType = it },
                    filterCategoryItem = filterCategoryItem,
                    onFilterCategoryItemChange = { filterCategoryItem = it },
                    filterTag = filterTag,
                    onFilterTagChange = { filterTag = it },
                    groups = viewModel.groups
                )
            }
        }

        // ========== BODY ==========
        if (!showForm) {
            val filtered = occurrences.filter { occ ->
                val matchesSearch = searchQuery.isEmpty() ||
                        occ.schedule.party.name.contains(searchQuery, ignoreCase = true) ||
                        occ.schedule.description.contains(searchQuery, ignoreCase = true) ||
                        occ.schedule.category.name.contains(searchQuery, ignoreCase = true) ||
                        occ.schedule.subcategory?.name?.contains(searchQuery, ignoreCase = true) == true ||
                        occ.schedule.tags?.any { it.name.contains(searchQuery, ignoreCase = true) } == true
                val matchesAccount = filterAccount == null || occ.schedule.account.id == filterAccount!!.id
                val matchesType = filterType == null || occ.schedule.type == filterType
                val matchesCategory = filterCategoryItem == null ||
                        (filterCategoryItem!!.second == null && occ.schedule.category.id == filterCategoryItem!!.first.id) ||
                        (filterCategoryItem!!.second != null && occ.schedule.subcategory?.id == filterCategoryItem!!.second!!.id)
                val matchesTag = filterTag == null || occ.schedule.tags?.any { it.id == filterTag!!.id } == true
                matchesSearch && matchesAccount && matchesType && matchesCategory && matchesTag
            }

            val startOfToday = today.atStartOfDay()
            val endOfToday = today.atTime(LocalTime.MAX)
            val endOfMonth = today.withDayOfMonth(today.lengthOfMonth()).atTime(LocalTime.MAX)

            val overdue = filtered.filter { it.dueDate.isBefore(startOfToday) }
            val dueToday = filtered.filter { !it.dueDate.isBefore(startOfToday) && !it.dueDate.isAfter(endOfToday) }
            val dueThisMonth = filtered.filter { it.dueDate.isAfter(endOfToday) && !it.dueDate.isAfter(endOfMonth) }
            val future = filtered.filter { it.dueDate.isAfter(endOfMonth) }

            val groups = listOf(
                "Atrasados" to overdue,
                "Vencendo hoje" to dueToday,
                "Vencendo este mês" to dueThisMonth,
                "Lançamentos futuros" to future,
            ).filter { it.second.isNotEmpty() }

            Box(modifier = Modifier.fillMaxSize()) {
                if (groups.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        TextH2(text = "Nenhum agendamento encontrado.")
                    }
                } else {
                    val listState = rememberLazyListState()
                    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                        item { Spacer(Modifier.height(30.dp)) }
                        groups.forEach { (title, items) ->
                            item {
                                ScheduleGroupHeader(modifier = Modifier.zIndex(1f), title = title)
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 90.dp)
                                        .zIndex(2f)
                                        .clip(RoundedCornerShape(topEnd = 0.dp, bottomStart = 0.dp))
                                        .background(
                                            MaterialTheme.colors.surface,
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
                                            viewModel = viewModel,
                                            occurrence = occ,
                                            overdue = title == "Atrasados",
                                            onEdit = {
                                                selectedSchedule = occ.schedule
                                                showForm = true
                                                newType = null
                                                backIcon = true
                                                addresses = initialAddress + PageAddress(
                                                    iconVector = PhosphorIcons.Regular.Pencil,
                                                    iconSize = DpSize(21.dp, 18.dp),
                                                    name = "Editar agendamento"
                                                )
                                            }
                                        )
                                    }
                                    Spacer(Modifier.height(20.dp))
                                }
                                Spacer(Modifier.height(30.dp))
                            }
                        }
                        item { Spacer(Modifier.height(50.dp)) }
                    }

                    VerticalScrollbar(
                        adapter = rememberScrollbarAdapter(listState),
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }

                AddScheduleButton(
                    onClickGain = {
                        newType = TransactionType.GAIN
                        selectedSchedule = null
                        showForm = true
                        backIcon = true
                        addresses = initialAddress + PageAddress(
                            iconVector = PhosphorIcons.Light.PlusSquare,
                            iconSize = DpSize(21.dp, 18.dp),
                            name = "Nova receita agendada"
                        )
                    },
                    onClickExpense = {
                        newType = TransactionType.EXPENSE
                        selectedSchedule = null
                        showForm = true
                        backIcon = true
                        addresses = initialAddress + PageAddress(
                            iconVector = PhosphorIcons.Light.MinusSquare,
                            iconSize = DpSize(21.dp, 18.dp),
                            name = "Nova despesa agendada"
                        )
                    },
                )
            }
        }

        if (showForm) {
            ScheduleForm(
                scheduleViewModel = viewModel,
                schedule = selectedSchedule,
                transactionType = newType,
                initialAccount = filterAccount,
                onDismiss = { saved ->
                    showForm = false
                    selectedSchedule = null
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
private fun AddScheduleButton(
    onClickGain: () -> Unit,
    onClickExpense: () -> Unit,
) {
    var showDropdown by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize().padding(bottom = 50.dp, end = 50.dp)) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(ButtonPurple)
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable { showDropdown = true }
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                modifier = Modifier.size(25.dp).align(Alignment.Center),
                imageVector = PhosphorIcons.Light.Plus,
                contentDescription = "Add schedule",
                tint = Color.White
            )
            if (showDropdown) {
                DropDownAddSchedule(
                    expanded = showDropdown,
                    onClickGain = { showDropdown = false; onClickGain() },
                    onClickExpense = { showDropdown = false; onClickExpense() },
                    onDismissRequest = { showDropdown = false }
                )
            }
        }
    }
}
