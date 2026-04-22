package view.modules.transactionForm

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.CaretDown
import com.adamglin.phosphoricons.light.Check
import domain.entity.Category
import domain.entity.Schedule
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.enums.CategoryType
import domain.enums.PartyType
import domain.enums.TransactionType
import utils.IconPaths
import utils.rememberSvgPainter
import utils.toBrMoney
import view.modules.transactionForm.components.RecurrencePicker
import view.modules.transactionForm.components.RecurrenceSetView
import view.modules.transactionForm.components.buildRecurrenceSummary
import view.modules.transactionForm.components.CategoriesPicker
import view.modules.transactionForm.components.PartiesPicker
import view.modules.transactionForm.components.TagsPicker
import view.shared.DateTimePicker
import view.shared.DefaultTextField
import view.shared.DropDownTextField
import view.shared.TagListView
import view.shared.TextNormal
import view.shared.TextSmall
import view.theme.ButtonGreen
import view.theme.Ubuntu
import viewModel.TransactionFormViewModel
import kotlin.math.abs

@Composable
fun TransactionForm(
    allAccounts: List<BankAccount> = emptyList(),
    schedule: Schedule? = null,
    transaction: Transaction? = null,
    scheduleFormViewModel: TransactionFormViewModel = remember { TransactionFormViewModel() },
    transactionType: TransactionType? = null,
    initialAccount: BankAccount? = null,
    lockAccount: Boolean = false,
    onDismiss: (Boolean) -> Unit,
) {
    LaunchedEffect(schedule, transaction) {
        when {
            schedule != null -> scheduleFormViewModel.loadFromSchedule(schedule)
            transaction != null -> scheduleFormViewModel.loadFromTransaction(transaction)
            else -> {
                scheduleFormViewModel.clear()
                scheduleFormViewModel.type = transactionType!!
                if (initialAccount != null) scheduleFormViewModel.account = initialAccount
            }
        }
    }

    val tags = scheduleFormViewModel.tags.collectAsState()
    val party = scheduleFormViewModel.party.collectAsState()
    val category = scheduleFormViewModel.category.collectAsState()
    val subcategory = scheduleFormViewModel.subCategory
    val accountState = scheduleFormViewModel.account

    val saveButtonActive by remember {
        derivedStateOf {
            party.value != null && category.value != null && scheduleFormViewModel.account.id != 0L
        }
    }

    val incomeGreen = MaterialTheme.colors.onPrimary
    val expenseRed = MaterialTheme.colors.onError

    val typeColor = derivedStateOf {
        when (scheduleFormViewModel.type) {
            TransactionType.EXPENSE -> expenseRed
            TransactionType.GAIN -> incomeGreen
            else -> Color.Gray
        }
    }

    val isTransactionEdit = transaction != null

    Box(modifier = Modifier.fillMaxSize()) {

        var showSide by remember { mutableStateOf(false) }
        var sideType by remember { mutableStateOf("") }
        val targetSize by derivedStateOf {
            when (sideType) {
                "tags" -> 850.dp
                "recurr" -> 950.dp
                else -> 1100.dp
            }
        }
        val dialogWidth by animateDpAsState(
            targetValue = if (showSide) targetSize else 550.dp,
            animationSpec = tween(durationMillis = 800)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(dialogWidth).align(Alignment.TopCenter).padding(top = 100.dp)
        ) {

            Row(
                modifier = Modifier
                    .width(550.dp)
                    .zIndex(2f)
                    .shadow(2.dp, RoundedCornerShape(10.dp))
                    .background(
                        MaterialTheme.colors.surface,
                        androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                    )
                    .border(
                        0.5.dp,
                        MaterialTheme.colors.onSurface,
                        androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                    )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 650.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 30.dp, vertical = 40.dp)
                ) {

                    //==== HEADER (type label + account)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextSmall(
                            text = scheduleFormViewModel.typeLabel.value,
                            color = typeColor.value
                        )
                        if (lockAccount) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                if (accountState.id != 0L) {
                                    Icon(
                                        painter = rememberSvgPainter(IconPaths.BANK_LOGOS + accountState.icon),
                                        contentDescription = null,
                                        tint = MaterialTheme.colors.primary,
                                        modifier = Modifier.size(20.dp),
                                    )
                                    Text(
                                        text = accountState.name,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colors.primary,
                                        fontWeight = FontWeight.Normal,
                                        lineHeight = 0.sp,
                                        fontFamily = Ubuntu
                                    )
                                }
                            }
                        } else {
                            AccountSelector(
                                currentAccount = accountState,
                                allAccounts = allAccounts,
                                onSelect = { scheduleFormViewModel.account = it }
                            )
                        }
                    }

                    Divider(Modifier.padding(top = 5.dp, bottom = 30.dp).background(typeColor.value))

                    //---start date + balance
                    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
                        DateTimePicker(
                            modifier = Modifier.weight(1f),
                            value = scheduleFormViewModel.startDate,
                            selectedDateTime = { scheduleFormViewModel.startDate = it }
                        )

                        val balance by remember { derivedStateOf { toBrMoney.format(abs(scheduleFormViewModel.balance)) } }
                        DefaultTextField(
                            modifier = Modifier.weight(1f).padding(start = 10.dp),
                            value = balance,
                            label = "Valor:",
                            textAlign = TextAlign.Right,
                            placeholder = "0.000,00"
                        ) { input ->
                            var filtered = input.filter { c -> c.isDigit() || c == ',' || c == '.' }
                            if (filtered.isEmpty() || filtered == ".") filtered = "0.00"
                            scheduleFormViewModel.updateBalance(filtered)
                        }
                    }

                    //---party
                    DropDownTextField(
                        modifier = Modifier.padding(bottom = 20.dp),
                        value = party.value?.name ?: "",
                        label = if (scheduleFormViewModel.type == TransactionType.GAIN) "Pagador" else "Recebedor",
                        placeholder = "Nome",
                        onClick = {
                            if (showSide && sideType == "parties") showSide = false
                            else if (showSide) sideType = "parties"
                            else {
                                sideType = "parties"; showSide = true
                            }
                        }
                    )

                    //---description
                    DefaultTextField(
                        modifier = Modifier.padding(bottom = 20.dp),
                        value = scheduleFormViewModel.description,
                        label = "Descrição:",
                        boxSize = 80.dp,
                        placeholder = "Informações adicionais"
                    ) { scheduleFormViewModel.description = it }

                    //---category
                    DropDownTextField(
                        modifier = Modifier.padding(bottom = 20.dp),
                        icon = category.value?.icon,
                        value = if (category.value?.name.isNullOrEmpty()) "" else category.value!!.name + if (subcategory?.name.isNullOrEmpty()) "" else " → ${subcategory.name}",
                        label = "Categoria:",
                        placeholder = "Selecione a categoria",
                        onClick = {
                            if (showSide && sideType == "categories") showSide = false
                            else if (showSide) sideType = "categories"
                            else {
                                sideType = "categories"; showSide = true
                            }
                        }
                    )

                    //---tags
                    TagListView(
                        label = "Etiquetas:",
                        placeholder = "Etiquetas",
                        tags = tags.value,
                        onClickAdd = {
                            if (showSide && sideType == "tags") showSide = false
                            else if (showSide) sideType = "tags"
                            else {
                                sideType = "tags"; showSide = true
                            }
                        }
                    )

                    //---recurrence (hidden when editing an existing transaction)
                    if (!isTransactionEdit) {
                        Spacer(Modifier.height(20.dp))
                        Divider()
                        Spacer(Modifier.height(20.dp))

                        RecurrenceSetView(
                            label = "Recorrência:",
                            summary = buildRecurrenceSummary(
                                frequency = scheduleFormViewModel.frequency,
                                interval = scheduleFormViewModel.interval,
                                installments = scheduleFormViewModel.installments,
                                endDate = scheduleFormViewModel.endDate,
                            ),
                            onClickEdit = {
                                if (showSide && sideType == "recurr") showSide = false
                                else if (showSide) sideType = "recurr"
                                else {
                                    sideType = "recurr"; showSide = true
                                }
                            }
                        )
                    }
                }
            }


            // SIDE PANEL
            AnimatedVisibility(visible = showSide, enter = fadeIn(tween(800)), exit = fadeOut(tween(800))) {
                Row(modifier = Modifier.height(450.dp).offset(x = (-1).dp).zIndex(1f)) {
                    when (sideType) {
                        "categories" ->
                            CategoriesPicker(
                                category = scheduleFormViewModel.category.value ?: Category(),
                                subcategory = scheduleFormViewModel.subCategory,
                                type = if (scheduleFormViewModel.type == TransactionType.GAIN) CategoryType.INCOME else CategoryType.EXPENSE,
                                onCategoryClick = { cat, sub ->
                                    scheduleFormViewModel.category.value = cat
                                    scheduleFormViewModel.subCategory = sub
                                }
                            )

                        "parties" ->
                            PartiesPicker(
                                partyType = if (scheduleFormViewModel.type == TransactionType.GAIN) PartyType.PAYER else PartyType.RECEIVER,
                                party = party.value,
                                onPartyClick = { scheduleFormViewModel.party.value = it }
                            )

                        "recurr" ->
                            RecurrencePicker(viewModel = scheduleFormViewModel)

                        else ->
                            TagsPicker(
                                selected = tags.value,
                                onTagClick = { scheduleFormViewModel.tags.value = it.toList() }
                            )
                    }
                }
            }
        }


        //==== SAVE BUTTON
        Button(
            enabled = saveButtonActive,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ButtonGreen,
                disabledBackgroundColor = MaterialTheme.colors.primaryVariant.copy(alpha = 0.5f)
            ),
            onClick = {
                scheduleFormViewModel.save()
                onDismiss(true)
            },
            shape = CircleShape,
            modifier = Modifier
                .padding(bottom = 50.dp, end = 50.dp).size(60.dp).align(Alignment.BottomEnd)
                .pointerHoverIcon(if (saveButtonActive) PointerIcon.Hand else PointerIcon.Default)
        ) {
            Icon(
                modifier = Modifier.size(25.dp),
                imageVector = PhosphorIcons.Light.Check,
                contentDescription = "Save",
                tint = Color.White
            )
        }
    }
}


@Composable
private fun AccountSelector(
    currentAccount: BankAccount,
    allAccounts: List<BankAccount>,
    onSelect: (BankAccount) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val label = if (currentAccount.id == 0L) "Selecione a conta" else currentAccount.name

    Box {
        Row(
            modifier = Modifier
                .height(28.dp)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(6.dp))
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable { expanded = true }
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (currentAccount.id != 0L) {
                Icon(
                    painter = rememberSvgPainter(IconPaths.BANK_LOGOS + currentAccount.icon),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = label,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                color = MaterialTheme.colors.primary,
                fontWeight = FontWeight.Normal,
                fontFamily = Ubuntu
            )
            Icon(
                imageVector = PhosphorIcons.Light.CaretDown,
                contentDescription = null,
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier.size(14.dp)
            )
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            allAccounts.forEach { acc ->
                DropdownMenuItem(onClick = {
                    onSelect(acc)
                    expanded = false
                }) {
                    TextNormal(text = acc.name)
                }
            }
        }
    }
}