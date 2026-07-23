package view.modules.transactionForm

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import domain.entity.Tag
import domain.entity.Transaction
import domain.entity.account.BankAccount
import domain.entity.Party
import domain.enums.CategoryType
import domain.enums.PartyType
import domain.enums.TransactionType
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import utils.IconPaths
import utils.rememberSvgPainter
import utils.formatNumber
import viewModel.UserPreferences
import view.modules.transactionForm.components.*
import view.shared.*
import view.theme.ButtonGreen
import view.theme.DefaultFont
import viewModel.TransactionFormViewModel
import kotlin.math.abs

@Composable
fun TransactionForm(
    allAccounts: List<BankAccount> = emptyList(),
    schedule: Schedule? = null,
    occurrenceIndex: Int? = null,
    transaction: Transaction? = null,
    scheduleFormViewModel: TransactionFormViewModel = remember { TransactionFormViewModel() },
    transactionType: TransactionType? = null,
    initialAccount: BankAccount? = null,
    lockAccount: Boolean = false,
    isTransfer: Boolean = false,
    onDismiss: (Boolean) -> Unit,
) {
    val transferDefaultDescription = stringResource(Res.string.transfer_default_description)

    LaunchedEffect(schedule, transaction, occurrenceIndex) {
        when {
            schedule != null -> {
                scheduleFormViewModel.loadFromSchedule(schedule)
                if (occurrenceIndex != null && schedule.installments != null) {
                    scheduleFormViewModel.installment = "${occurrenceIndex + 1}/${schedule.installments}"
                }
            }
            transaction != null -> scheduleFormViewModel.loadFromTransaction(transaction)
            else -> {
                scheduleFormViewModel.clear()
                if (isTransfer) {
                    scheduleFormViewModel.isTransfer = true
                    scheduleFormViewModel.description = transferDefaultDescription
                    if (initialAccount != null) scheduleFormViewModel.account = initialAccount
                } else {
                    scheduleFormViewModel.type = transactionType!!
                    if (initialAccount != null) scheduleFormViewModel.account = initialAccount
                }
            }
        }
    }

    val party by scheduleFormViewModel.party.collectAsState()
    val category by scheduleFormViewModel.category.collectAsState()
    val tags by scheduleFormViewModel.tags.collectAsState()

    val saveButtonActive by remember {
        derivedStateOf {
            if (scheduleFormViewModel.isTransfer) {
                scheduleFormViewModel.account.id != 0L &&
                scheduleFormViewModel.destinationAccount.id != 0L &&
                scheduleFormViewModel.account.id != scheduleFormViewModel.destinationAccount.id &&
                abs(scheduleFormViewModel.balance) > 0
            } else {
                party != null && category != null && scheduleFormViewModel.account.id != 0L
            }
        }
    }

    val typeColor = when {
        scheduleFormViewModel.isTransfer -> MaterialTheme.colors.primary
        scheduleFormViewModel.type == TransactionType.EXPENSE -> MaterialTheme.colors.onError
        scheduleFormViewModel.type == TransactionType.GAIN -> MaterialTheme.colors.onPrimary
        else -> Color.Gray
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
                    .background(
                        MaterialTheme.colors.background.copy(0.6f),
                        RoundedCornerShape(10.dp)
                    )
                    .border(
                        0.5.dp,
                        MaterialTheme.colors.onSurface,
                        RoundedCornerShape(10.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 700.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 30.dp, vertical = 40.dp)
                ) {
                    Header(
                        lockAccount = lockAccount,
                        accountState = scheduleFormViewModel.account,
                        allAccounts = allAccounts,
                        typeColor = typeColor,
                        onAccountSelect = { scheduleFormViewModel.account = it },
                        isTransfer = scheduleFormViewModel.isTransfer,
                        destinationAccount = scheduleFormViewModel.destinationAccount,
                        onDestinationAccountSelect = { scheduleFormViewModel.destinationAccount = it },
                    )

                    FormFields(
                        viewModel = scheduleFormViewModel,
                        party = party,
                        category = category,
                        tags = tags,
                        isTransactionEdit = isTransactionEdit,
                        schedule = schedule,
                        occurrenceIndex = occurrenceIndex,
                        onToggleSide = { type ->
                            if (showSide && sideType == type) showSide = false
                            else if (showSide) sideType = type
                            else { sideType = type; showSide = true }
                        },
                        isTransfer = scheduleFormViewModel.isTransfer,
                    )
                }
            }

            SidePanel(
                visible = showSide,
                sideType = sideType,
                viewModel = scheduleFormViewModel,
            )
        }

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
                contentDescription = stringResource(Res.string.save),
                tint = Color.White
            )
        }
    }
}

@Composable
private fun Header(
    lockAccount: Boolean,
    accountState: BankAccount,
    allAccounts: List<BankAccount>,
    typeColor: Color,
    onAccountSelect: (BankAccount) -> Unit,
    isTransfer: Boolean = false,
    destinationAccount: BankAccount = BankAccount(),
    onDestinationAccountSelect: (BankAccount) -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isTransfer) Arrangement.SpaceBetween else Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isTransfer) {
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
                        fontFamily = DefaultFont
                    )
                }
            }
            Icon(
                imageVector = PhosphorIcons.Light.CaretDown,
                contentDescription = null,
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier.size(16.dp).graphicsLayer(rotationZ = -90f)
            )
            AccountSelector(
                currentAccount = destinationAccount,
                allAccounts = allAccounts.filter { it.id != accountState.id },
                onSelect = onDestinationAccountSelect,
                placeholder = stringResource(Res.string.form_field_destination_account),
            )
        } else if (lockAccount) {
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
                        fontFamily = DefaultFont
                    )
                }
            }
        } else {
            AccountSelector(
                currentAccount = accountState,
                allAccounts = allAccounts,
                onSelect = onAccountSelect
            )
        }
    }
    Divider(Modifier.padding(top = 5.dp, bottom = 30.dp).background(typeColor))
}

@Composable
private fun FormFields(
    viewModel: TransactionFormViewModel,
    party: Party?,
    category: Category?,
    tags: List<Tag>,
    isTransactionEdit: Boolean,
    schedule: Schedule?,
    occurrenceIndex: Int?,
    onToggleSide: (String) -> Unit,
    isTransfer: Boolean = false,
) {
    val subcategory = viewModel.subCategory

    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
        DateTimePicker(
            modifier = Modifier.weight(1f),
            value = viewModel.startDate,
            selectedDateTime = { viewModel.startDate = it }
        )

        val balance by remember { derivedStateOf { formatNumber(abs(viewModel.balance)) } }
        val balancePlaceholder by remember {
            derivedStateOf {
                when (UserPreferences.currencyFormat) {
                    "comma-dot" -> "0,000.00"
                    "plain-dot" -> "0.00"
                    else -> "0.000,00"
                }
            }
        }
        DefaultTextField(
            modifier = Modifier.weight(1f).padding(start = 10.dp),
            value = balance,
            label = stringResource(Res.string.form_field_amount),
            textAlign = TextAlign.Right,
            placeholder = balancePlaceholder
        ) { input ->
            var filtered = input.filter { c -> c.isDigit() || c == ',' || c == '.' }
            if (filtered.isEmpty() || filtered == ".") filtered = "0.00"
            viewModel.updateBalance(filtered)
        }
    }

    if (!isTransfer) {
        DropDownTextField(
            modifier = Modifier.padding(bottom = 20.dp),
            value = party?.name ?: "",
            label = if (viewModel.type == TransactionType.GAIN) stringResource(Res.string.form_field_payer) else stringResource(Res.string.form_field_receiver),
            placeholder = stringResource(Res.string.name),
            onClick = { onToggleSide("parties") }
        )
    }

    DefaultTextField(
        modifier = Modifier.padding(bottom = 20.dp),
        value = viewModel.description,
        label = stringResource(Res.string.form_field_description),
        boxSize = 80.dp,
        placeholder = stringResource(Res.string.additional_info)
    ) { viewModel.description = it }

    if (!isTransfer) {
        DropDownTextField(
            modifier = Modifier.padding(bottom = 20.dp),
            icon = category?.icon,
            value = if (category?.name.isNullOrEmpty()) "" else category.name + if (subcategory?.name.isNullOrEmpty()) "" else " → ${subcategory.name}",
            label = stringResource(Res.string.form_field_category),
            placeholder = stringResource(Res.string.form_placeholder_select_category),
            onClick = { onToggleSide("categories") }
        )

        TagListView(
            label = stringResource(Res.string.form_field_tags),
            placeholder = stringResource(Res.string.form_placeholder_tags),
            tags = tags,
            onClickAdd = { onToggleSide("tags") }
        )

        if (!isTransactionEdit) {
            Spacer(Modifier.height(20.dp))
            Divider(color = MaterialTheme.colors.onSurface)
            Spacer(Modifier.height(20.dp))

            RecurrenceSetView(
                label = stringResource(Res.string.form_field_recurrence),
                summary = buildRecurrenceSummary(
                    frequency = viewModel.frequency,
                    interval = viewModel.interval,
                    installments = viewModel.installments,
                    endDate = viewModel.endDate,
                ),
                onClickEdit = { onToggleSide("recurr") }
            )

            if (schedule != null && schedule.installments != null && occurrenceIndex != null) {
                Spacer(Modifier.height(20.dp))
                Divider(color = MaterialTheme.colors.onSurface)
                Spacer(Modifier.height(20.dp))
                InstallmentView(installment = viewModel.installment)
            }
        } else {
            if (viewModel.scheduleId != null) {
                Spacer(Modifier.height(20.dp))
                Divider()
                Spacer(Modifier.height(20.dp))
                InstallmentView(installment = viewModel.installment)
            }
        }
    }
}

@Composable
private fun SidePanel(
    visible: Boolean,
    sideType: String,
    viewModel: TransactionFormViewModel,
) {
    AnimatedVisibility(visible = visible, enter = fadeIn(tween(800)), exit = fadeOut(tween(800))) {
        Row(modifier = Modifier.height(450.dp).offset(x = (-1).dp).zIndex(1f)) {
            when (sideType) {
                "categories" ->
                    CategoriesPicker(
                        category = viewModel.category.value ?: Category(),
                        subcategory = viewModel.subCategory,
                        type = if (viewModel.type == TransactionType.GAIN) CategoryType.INCOME else CategoryType.EXPENSE,
                        onCategoryClick = { cat, sub ->
                            viewModel.setCategory(cat)
                            viewModel.subCategory = sub
                        }
                    )

                "parties" ->
                    PartiesPicker(
                        partyType = if (viewModel.type == TransactionType.GAIN) PartyType.PAYER else PartyType.RECEIVER,
                        party = viewModel.party.value,
                        onPartyClick = { viewModel.setParty(it) }
                    )

                "recurr" ->
                    RecurrencePicker(viewModel = viewModel)

                else ->
                    TagsPicker(
                        selected = viewModel.tags.value,
                        onTagClick = { viewModel.setTags(it.toList()) }
                    )
            }
        }
    }
}

@Composable
private fun AccountSelector(
    currentAccount: BankAccount,
    allAccounts: List<BankAccount>,
    onSelect: (BankAccount) -> Unit,
    placeholder: String = stringResource(Res.string.form_placeholder_select_account),
) {
    var expanded by remember { mutableStateOf(false) }
    val label = if (currentAccount.id == 0L) placeholder else currentAccount.name

    Box {
        Row(
            modifier = Modifier
                .height(28.dp)
                .clip(RoundedCornerShape(6.dp))
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
                fontFamily = DefaultFont
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
