package view.modules.transactionForm

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import core.entity.Transaction
import core.entity.account.BankAccount
import core.enums.PartyType
import core.enums.TransactionType
import utils.IconPaths
import utils.toBrMoney
import view.modules.transactionForm.components.*
import view.shared.*
import view.theme.Ubuntu
import viewModel.TransactionFormViewModel
import kotlin.math.abs


@Composable
fun TransactionForm(
    account: BankAccount,
    transaction: Transaction? = null,
    transactionFormViewModel: TransactionFormViewModel = remember { TransactionFormViewModel(transaction) },
    transactionType: TransactionType? = null,
    onDismiss: () -> Unit
) {
    val tags = transactionFormViewModel.tags.collectAsState()
    val party = transactionFormViewModel.party.collectAsState()

    if (transaction == null && transactionType != null)  transactionFormViewModel.type = transactionType

    var showSide by remember { mutableStateOf(false) }
    var sideType by remember{ mutableStateOf("") }
    val targetSize by derivedStateOf {
        if (sideType == "tags") 850.dp else 1200.dp
    }

    val dialogWidth by animateDpAsState(
        targetValue = if (showSide) targetSize else 560.dp,
        animationSpec = tween(durationMillis = 800) // Duração de 500ms
    )
    val title by remember{ derivedStateOf { if (transactionFormViewModel.id == 0L) "Adicionar transação" else "Editar transação" }}

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(dialogWidth).background(MaterialTheme.colors.background, shape = RoundedCornerShape(8.dp))
        ) {
            DialogTitleBar(title, onDismiss)

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                .padding(vertical = 30.dp).padding(horizontal = 30.dp)
                .height(550.dp)
            ) {

                Row(modifier = Modifier
                    .width(500.dp)
                    .zIndex(2f)
                    .shadow(2.dp, RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colors.onPrimary, RoundedCornerShape(10.dp))
                    .border(1.dp, transactionFormViewModel.typeColor.value, RoundedCornerShape(10.dp))
                ) {
                    //==== FORM
                    Column(modifier = Modifier.fillMaxWidth()

                    ) {
                        Column(Modifier.fillMaxWidth().padding(30.dp)) {

                            //==== ACCOUNT ICON AND NAME
                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                                Icon(
                                    painter = painterResource(IconPaths.BANK_LOGOS + account.icon),
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.primary,
                                    modifier = Modifier.size(20.dp),
                                )
                                Text(
                                    modifier = Modifier.padding(horizontal = 5.dp),
                                    text = account.name,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colors.primary,
                                    fontWeight = FontWeight.Normal,
                                    lineHeight = 0.sp,
                                    fontFamily = Ubuntu
                                )
                            }
                            Divider(Modifier.padding(top = 5.dp, bottom = 20.dp))


                            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {

                                //---date
                                DateTimePicker(
                                    modifier = Modifier.weight(1f),
                                    value = transactionFormViewModel.date,
                                    selectedDateTime = { transactionFormViewModel.date = it }
                                )

                                //---balance
                                var balance by remember { mutableStateOf(toBrMoney.format(abs(transactionFormViewModel.balance))) }
                                DefaultTextField(
                                    modifier = Modifier.weight(1f).padding(start = 10.dp),
                                    value = balance,
                                    label = "Valor:",
                                    textAlign = TextAlign.Right,
                                    placeholder = "0.000,00"
                                ) {
                                    balance = it.filter { char -> char.isDigit() || char == ',' || char == '.' }
                                    balance = if (balance.isNullOrEmpty() || balance == ".") "0.00" else balance
                                    transactionFormViewModel.updateBalance(balance)
                                }
                            }

                            //---party
                            DefaultPartyField(
                                modifier = Modifier.padding(bottom = 20.dp),
                                value = party.value.name,
                                label = if (transactionFormViewModel.type == TransactionType.GAIN) "Pagador" else "Recebedor",
                                placeholder = "Nome",
                                onValueChange = { transactionFormViewModel.party.value.name = it },
                                onClick = {
                                    if (showSide && sideType == "parties")
                                        showSide = false
                                    else if (showSide)
                                        sideType = "parties"
                                    else
                                    {
                                        sideType = "parties"
                                        showSide = true
                                    }
                                }
                            )


                            //---description
                            DefaultTextField(
                                modifier = Modifier.padding(bottom = 20.dp),
                                value = transactionFormViewModel.description,
                                label = "Descrição:",
                                boxSize = 80.dp,
                                placeholder = "Informações adicionais"
                            ) { transactionFormViewModel.description = it }


                            //---category
                            val category = transactionFormViewModel.category
                            val subcategory = transactionFormViewModel.subCategory
                            DropDownTextField(
                                modifier = Modifier.padding(bottom = 20.dp),
                                categoryIcon = category.icon,
                                value = category.name + if (subcategory?.name.isNullOrEmpty()) "" else " → ${subcategory?.name}",
                                label = "Categoria:",
                                placeholder = "Selecione a categoria",
                                onClick = {
                                    if (showSide && sideType == "categories")
                                        showSide = false
                                    else if (showSide)
                                        sideType = "categories"
                                    else
                                    {
                                        sideType = "categories"
                                        showSide = true
                                    }
                                }
                            )


                            //---tags
                            TagListView(
                                label = "Etiquetas:",
                                placeholder = "Etiquetas",
                                tags = tags.value,
                                onClickTag = { },
                                onClickAdd = {
                                    if (showSide && sideType == "tags")
                                        showSide = false
                                    else if (showSide)
                                        sideType = "tags"
                                    else
                                    {
                                        sideType = "tags"
                                        showSide = true
                                    }
                                }
                            )

                        }
                    }
                }


                AnimatedVisibility(visible = showSide, enter = fadeIn(tween(800)), exit = fadeOut(tween(800))) {


                    Row(modifier = Modifier
                        .fillMaxHeight(0.8f)
                        .offset (x = (-1).dp)
                        .zIndex(1f)
                    ) {

                        when (sideType) {
                            "categories" ->
                                CategoriesPicker(
                                    category = transactionFormViewModel.category,
                                    subcategory = transactionFormViewModel.subCategory,
                                    onCategoryClick = { category, subcategory ->
                                        transactionFormViewModel.category = category
                                        transactionFormViewModel.subCategory = subcategory
                                    }
                                )

                            "parties" ->
                                PartiesPicker(
                                    partyType = if (transactionFormViewModel.type == TransactionType.GAIN) PartyType.PAYER else PartyType.RECEIVER,
                                    party = party.value,
                                    onPartyClick = { transactionFormViewModel.party.value = it }
                                )
                            else ->
                                TagsPicker(
                                    selected = tags.value,
                                    onTagClick = { transactionFormViewModel.tags.value = it.toList()}
                                )
                        }
                    }
                }
            }


            //==== FOOTER
            Divider()
            Box(Modifier.padding(20.dp, vertical = 10.dp)) {
                DefaultButton(confirmed = true, onClick = {}, label = title)
//                val confirmed by remember { derivedStateOf { transactionViewModel.name != "" && transactionViewModel.group.id != 0L } }
//
//                        DefaultButton(confirmed = confirmed, buttonLabel) {
//                            addAccountViewModel.saveCheckingAccount(transaction)
//                            transactionViewModel.loadGroup()
//                            onDismiss()
//                        }
            }
        }
    }
}