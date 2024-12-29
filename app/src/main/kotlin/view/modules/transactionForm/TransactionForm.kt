package view.modules.transactionForm

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
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
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Shapes
import core.entity.Transaction
import core.entity.account.BankAccount
import core.enums.TransactionType
import utils.IconPaths
import utils.toBrMoney
import view.modules.categories.components.CategoryListItem
import view.modules.transactionForm.components.DefaultPartyField
import view.modules.transactionForm.components.PartiesDialog
import view.modules.transactionForm.components.TagsDialog
import view.modules.transactionForm.components.TypeListComboBox
import view.shared.*
import view.theme.Ubuntu
import viewModel.CategoryViewModel
import viewModel.TransactionFormViewModel
import kotlin.math.abs


@Composable
fun TransactionForm(
    transactionFormViewModel: TransactionFormViewModel = TransactionFormViewModel(),
    account: BankAccount,
    transaction: Transaction? = null,
    onDismiss: () -> Unit
) {

    if (transaction != null) transactionFormViewModel.initializeFromTransaction(transaction)
    var showSide by remember { mutableStateOf(false) }
    var sideType by remember{ mutableStateOf("") }

    val dialogWidth by animateDpAsState(
        targetValue = if (showSide) 1100.dp else 560.dp,
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


                            //---type
                            TypeListComboBox(
                                modifier = Modifier.padding(bottom = 20.dp),
                                value = transactionFormViewModel.typeLabel.value,
                                label = "Tipo:",
                                placeholder = "Selecione o tipo",
                                onClickItem = { selectecType ->
                                    if (selectecType != transactionFormViewModel.type)
                                        transactionFormViewModel.type = selectecType
                                    transactionFormViewModel.updateBalance()
                                }
                            )

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
                                value = transactionFormViewModel.party.name,
                                label = if (transactionFormViewModel.type == TransactionType.GAIN) "Pagador" else "Recebedor",
                                placeholder = "Nome",
                                onValueChange = { transactionFormViewModel.party.name = it },
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
                            val category by remember { derivedStateOf { transactionFormViewModel.category } }
                            val subcategory by remember { derivedStateOf { transactionFormViewModel.subCategory } }
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
                                tags = transactionFormViewModel.tags,
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
                        .fillMaxHeight(0.9f)
                        .offset (x = (-1).dp)
                        .zIndex(1f)
                    ) {

                        when (sideType) {
                            "categories" -> {



/************************************ CATEGORIES DIALOG ************************************/

//                                CategoriesDialog(
//                                    category = transactionFormViewModel.category,
//                                    subcategory = transactionFormViewModel.subCategory,
//                                    onCategoryClick = { category, subcategory ->
//                                        transactionFormViewModel.selectCategory(category)
//                                        transactionFormViewModel.subCategory = subcategory
//                                    }
//                                )


                                val viewModel = remember { CategoryViewModel() }
                                viewModel.service.loadCategories(transactionFormViewModel.category.type)
                                viewModel.service.loadSubCategories(transactionFormViewModel.category)

                                val corner = 30.dp
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            MaterialTheme.colors.onPrimary,
                                            RoundedCornerShape(topEnd = corner, bottomEnd = corner)
                                        )
                                        .border(
                                            1.dp,
                                            MaterialTheme.colors.primaryVariant,
                                            RoundedCornerShape(topEnd = corner, bottomEnd = corner)
                                        )
                                ) {

                                    Row(modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp)) {

                                        //===== CATEGORIES

                                        val categories by viewModel.categories.collectAsState()
                                        val subCategories by viewModel.subCategories.collectAsState()


                                        Row(modifier = Modifier.width(260.dp).fillMaxHeight()) {

                                            Box(
                                                modifier = Modifier.weight(1f).fillMaxHeight().padding(vertical = 10.dp)
                                            ) {
                                                val listState = rememberLazyListState()


                                                LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {

                                                    items(categories, key = { it.id }) { item ->

                                                        val deleteDialogIsVisible = remember { mutableStateOf(false) }

                                                        CategoryListItem(
                                                            label = item.name,
                                                            icon = IconPaths.CATEGORY_PACK + item.icon,
                                                            clickableIcon = true,
                                                            hasSubItem = item.subcategories.size > 0,
                                                            isActive = item.id == transactionFormViewModel.category.id,
                                                            deleteDialogIsVisible = deleteDialogIsVisible,
                                                            onUpdateConfirmation = {
                                                                viewModel.service.updateCategory(
                                                                    item,
                                                                    it,
                                                                    item.icon
                                                                )
                                                            },
                                                            onSelectIcon = {
                                                                viewModel.service.updateCategory(
                                                                    item,
                                                                    item.name,
                                                                    it
                                                                )
                                                            },
                                                            /******************************** CATEGORY CLICK ********************************/
                                                            onContentClick = {
                                                                //Set selected Category and clean the selected Subcategory
                                                                transactionFormViewModel.selectCategory(item)
                                                                transactionFormViewModel.subCategory = null


                                                                //load subcategories list
                                                                viewModel.service.loadSubCategories(item)
                                                            },
                                                            /********************************************************************************/
                                                            deleteDialog = {
                                                                DialogDelete(
                                                                    title = "Excluir categoria",
                                                                    icon = PhosphorIcons.Light.Shapes,
                                                                    objectName = item.name,
                                                                    alertText = "Isso irá excluir permanentemente a categoria ${item.name} e remover todas as associações feitas à ela.",
                                                                    onClickButton = {
                                                                        viewModel.service.deleteCategory(
                                                                            item
                                                                        )
                                                                    },
                                                                    onDismiss = { deleteDialogIsVisible.value = false }
                                                                )
                                                            }
                                                        )

                                                    }

                                                    item {
                                                        val value = remember { mutableStateOf("") }
                                                        val isVisible = remember { mutableStateOf(false) }
                                                        AddListItem(
                                                            isVisible = isVisible,
                                                            value = value,
                                                            confirmationClick = {
                                                                viewModel.service.addCategory(
                                                                    type = viewModel.selectedCategory.value.type,
                                                                    name = value.value,
                                                                    icon = "question-mark.svg"
                                                                )
                                                            },
                                                        )
                                                    }

                                                }
                                                VerticalScrollbar(
                                                    adapter = rememberScrollbarAdapter(listState),
                                                    modifier = Modifier.align(Alignment.CenterEnd)
                                                )
                                            }
                                        }
                                        Divider(modifier = Modifier.width(2.dp).fillMaxHeight())


                                        //===== SUBCATEGORIES
                                        Row(modifier = Modifier.weight(1f).fillMaxHeight()) {

                                            Box(
                                                modifier = Modifier
                                                    .fillMaxHeight()
                                                    .padding(top = 10.dp)
                                            ) {
                                                val listState = rememberLazyListState()

                                                LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {

                                                    /******************************** LOAD SUBCATEGORIES LIST ********************************/
                                                    items(subCategories,
                                                        key = { it.id }) { subcategory ->

                                                        val deleteDialogIsVisible = remember { mutableStateOf(false) }
                                                        ListItem(
                                                            label = subcategory.name,
                                                            hasSubItem = false,
                                                            spaceBetween = 0.dp,
                                                            isActive = transactionFormViewModel.subCategory?.id == subcategory.id,
                                                            deleteDialogIsVisible = deleteDialogIsVisible,
                                                            onUpdateConfirmation = { /*viewModel.updateSubcategory(subcategory, it)*/ },
                                                            onContentClick = {
                                                                transactionFormViewModel.subCategory = subcategory; Unit
                                                            },
                                                            deleteDialog = {
                                                                DialogDelete(
                                                                    title = "Excluir subcategoria",
                                                                    icon = PhosphorIcons.Light.Shapes,
                                                                    objectName = subcategory.name,
                                                                    alertText = "Isso irá excluir permanentemente a subcategoria ${subcategory.name} e remover todas as associações feitas à ela.",
                                                                    onClickButton = {
                                                                        viewModel.service.deleteSubcategory(
                                                                            subcategory
                                                                        )
                                                                    },
                                                                    onDismiss = { deleteDialogIsVisible.value = false }
                                                                )
                                                            }
                                                        )

                                                    }

                                                    item {
                                                        val value = remember { mutableStateOf("") }
                                                        val isVisible = remember { mutableStateOf(false) }
                                                        AddListItem(
                                                            isVisible = isVisible,
                                                            value = value,
                                                            confirmationClick = {
                                                                viewModel.service.addSubcategory(
                                                                    name = value.value,
                                                                    category = viewModel.selectedCategory.value
                                                                )
                                                            },
                                                        )
                                                    }

                                                }
                                                VerticalScrollbar(
                                                    adapter = rememberScrollbarAdapter(listState),
                                                    modifier = Modifier.align(Alignment.CenterEnd)
                                                )
                                            }
                                        }

                                    }
                                }

                            }













/************************************ END CATEGORIES DIALOG ************************************/








                            "parties" -> PartiesDialog(viewModel = transactionFormViewModel)
                            else -> TagsDialog(viewModel = transactionFormViewModel)
                        }

                    }


                }


            }





            //==== FOOTER
            Divider()
            Box(Modifier.padding(20.dp, vertical = 10.dp)) {
                //val confirmed by remember { derivedStateOf { transactionViewModel.name != "" && transactionViewModel.group.id != 0L } }

//                        DefaultButton(confirmed = confirmed, buttonLabel) {
//                            addAccountViewModel.saveCheckingAccount(transaction)
//                            transactionViewModel.loadGroup()
//                            onDismiss()
//                        }

                // 👇🏻 Apagar depois
                DefaultButton(confirmed = true, title) { onDismiss() }

            }
        }
    }
}