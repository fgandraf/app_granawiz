package view.modules.transactionForm

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Check
import core.entity.Category
import core.entity.Transaction
import core.entity.account.BankAccount
import core.enums.CategoryType
import core.enums.PartyType
import core.enums.TransactionType
import utils.IconPaths
import utils.toBrMoney
import view.modules.transactionForm.components.CategoriesPicker
import view.modules.transactionForm.components.PartiesPicker
import view.modules.transactionForm.components.TagsPicker
import view.shared.*
import view.theme.ButtonGreen
import view.theme.Ubuntu
import viewModel.TransactionFormViewModel
import kotlin.math.abs


@Composable
fun TransactionForm(
    account: BankAccount,
    transaction: Transaction? = null,
    transactionFormViewModel: TransactionFormViewModel = remember { TransactionFormViewModel() },
    transactionType: TransactionType? = null,
    onDismiss: (Boolean, BankAccount) -> Unit
) {
    LaunchedEffect(transaction) {
        if (transaction != null) transactionFormViewModel.loadFromTransaction(transaction)
        else {
            transactionFormViewModel.clear()
            transactionFormViewModel.type = transactionType!!
            transactionFormViewModel.account = account
        }
    }

    val tags = transactionFormViewModel.tags.collectAsState()
    val party = transactionFormViewModel.party.collectAsState()
    val category = transactionFormViewModel.category.collectAsState()
    val subcategory = transactionFormViewModel.subCategory

    val saveButtonActive by remember { derivedStateOf { party.value != null && category.value != null } }

    val incomeGreen = MaterialTheme.colors.onPrimary
    val expenseRed = MaterialTheme.colors.onError

    val typeColor = derivedStateOf {
        when (transaction?.type) {
            TransactionType.EXPENSE -> expenseRed
            TransactionType.GAIN -> incomeGreen
            TransactionType.NEUTRAL -> Color.Gray
            null -> Color.White
        }
    }


    // BACKGROUND
    Box(modifier = Modifier.fillMaxSize()) {

        // FORM BOX
        var showSide by remember { mutableStateOf(false) }
        var sideType by remember{ mutableStateOf("") }
        val targetSize by derivedStateOf {
            if (sideType == "tags") 850.dp else 1100.dp
        }
        val dialogWidth by animateDpAsState(
            targetValue = if (showSide) targetSize else 550.dp,
            animationSpec = tween(durationMillis = 800)
        )
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.width(dialogWidth).align(Alignment.TopCenter).padding(top = 50.dp)) {


            // FIRST COLUMN: FORM
            Row(
                modifier = Modifier
                    .width(550.dp)
                    .zIndex(2f)
                    .shadow(2.dp, RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colors.surface, RoundedCornerShape(10.dp))
                    .border(0.5.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(10.dp))
            ) {

                //==== FORM
                Column(Modifier.fillMaxWidth().padding(horizontal = 30.dp, vertical = 40.dp)) {

                    //==== ACCOUNT ICON AND NAME
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row {
                            TextSmall(
                                text = transactionFormViewModel.typeLabel.value,
                                color = typeColor.value
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
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
                    }

                    Divider(Modifier.padding(top = 5.dp, bottom = 40.dp).background(typeColor.value))


                    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {

                        //---date
                        DateTimePicker(
                            modifier = Modifier.weight(1f),
                            value = transactionFormViewModel.date,
                            selectedDateTime = { transactionFormViewModel.date = it }
                        )

                        //---balance
                        val balance by remember { derivedStateOf { toBrMoney.format(abs(transactionFormViewModel.balance)) } }
                        DefaultTextField(
                            modifier = Modifier.weight(1f).padding(start = 10.dp),
                            value = balance,
                            label = "Valor:",
                            textAlign = TextAlign.Right,
                            placeholder = "0.000,00"
                        ) { input ->
                            var filtered = input.filter { char -> char.isDigit() || char == ',' || char == '.' }
                            if (filtered.isEmpty() || filtered == ".") filtered = "0.00"
                            transactionFormViewModel.updateBalance(filtered)
                        }
                    }

                    //---party
                    DropDownTextField(
                        modifier = Modifier.padding(bottom = 20.dp),
                        value = party.value?.name ?: "",
                        label = if (transactionFormViewModel.type == TransactionType.GAIN) "Pagador" else "Recebedor",
                        placeholder = "Nome",
                        onClick = {
                            if (showSide && sideType == "parties")
                                showSide = false
                            else if (showSide)
                                sideType = "parties"
                            else {
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

                    DropDownTextField(
                        modifier = Modifier.padding(bottom = 20.dp),
                        icon = category.value?.icon,
                        value = if (category.value?.name.isNullOrEmpty()) "" else category.value!!.name + if (subcategory?.name.isNullOrEmpty()) "" else " → ${subcategory?.name}",
                        label = "Categoria:",
                        placeholder = "Selecione a categoria",
                        onClick = {
                            if (showSide && sideType == "categories")
                                showSide = false
                            else if (showSide)
                                sideType = "categories"
                            else {
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
                        onClickAdd = {
                            if (showSide && sideType == "tags")
                                showSide = false
                            else if (showSide)
                                sideType = "tags"
                            else {
                                sideType = "tags"
                                showSide = true
                            }
                        }
                    )

                }

            }




            // SECOND COLUMN: SIDE
            AnimatedVisibility(visible = showSide, enter = fadeIn(tween(800)), exit = fadeOut(tween(800))) {
                Row(modifier = Modifier.height(450.dp).offset(x = (-1).dp).zIndex(1f)) {

                    when (sideType) {
                        "categories" ->
                            CategoriesPicker(
                                category = transactionFormViewModel.category.value ?: Category(),
                                subcategory = transactionFormViewModel.subCategory,
                                type = if (transactionType == TransactionType.GAIN) CategoryType.INCOME else CategoryType.EXPENSE,
                                onCategoryClick = { category, subcategory ->
                                    transactionFormViewModel.category.value = category
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
                                onTagClick = { transactionFormViewModel.tags.value = it.toList() }
                            )
                    }
                }
            }
        } // END: "FORM BOX"



        //==== FOOTER
        Button(
            enabled = saveButtonActive,
            colors = ButtonDefaults.buttonColors(backgroundColor = ButtonGreen),
            onClick = {
                transactionFormViewModel.saveTransaction()
                if (transactionFormViewModel.balance != transaction?.balance) onDismiss(true, account)
                else onDismiss(false, BankAccount())
                      },
            shape = CircleShape,
            modifier = Modifier
                .padding(bottom = 50.dp, end = 50.dp).size(60.dp).align(Alignment.BottomEnd)
                .pointerHoverIcon(if(saveButtonActive) PointerIcon.Hand else PointerIcon.Default)
        ){
            Icon(
                modifier = Modifier.size(25.dp),
                imageVector = PhosphorIcons.Light.Check,
                contentDescription = "Save transaction",
                tint = Color.White
            )
        }


    } // END "BACKGROUND"

}