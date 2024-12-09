package view.modules.transactionForm.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Shapes
import utils.IconPaths
import view.modules.categories.components.CategoryListItem
import view.shared.AddListItem
import view.shared.DialogDelete
import viewModel.TransactionFormViewModel


@Composable
fun CategoriesDialog(
    viewModel: TransactionFormViewModel,
    onDismissRequest: () -> Unit
) {

    var addCategoryButton by remember { mutableStateOf<Boolean>(false) }
    var addSubcategoryButton by remember { mutableStateOf<Boolean>(false) }
    viewModel.loadCategories()
    addCategoryButton = true


    Dialog(onDismissRequest = onDismissRequest ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().height(500.dp).background(MaterialTheme.colors.background, shape = RoundedCornerShape(8.dp))
        ) {

            Row {

                //===== CATEGORIES
                Row(modifier = Modifier.weight(1f).fillMaxHeight()) {

                    Box(
                        modifier = Modifier.weight(1f).fillMaxHeight().padding(vertical = 10.dp)
                    ) {
                        val listState = rememberLazyListState()

                        LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {

                            items(viewModel.categories, key = { it.id }) { category ->

                                val deleteDialogIsVisible = remember { mutableStateOf(false) }

                                CategoryListItem(
                                    label = category.name,
                                    icon = IconPaths.CATEGORY_PACK + category.icon,
                                    clickableIcon = true,
                                    hasSubItem = category.subcategories.size > 0,
                                    deleteDialogIsVisible = deleteDialogIsVisible,
                                    onUpdateConfirmation = { viewModel.updateCategory(category, it, category.icon) },
                                    onSelectIcon = { viewModel.updateCategory(category, category.name, it) },
                                    onContentClick = {
                                        viewModel.loadSubCategories(category)
                                        viewModel.selectCategory(category)
                                        addSubcategoryButton = true
                                        Unit
                                    },
                                    deleteDialog = {
                                        DialogDelete(
                                            title = "Excluir categoria",
                                            icon = PhosphorIcons.Light.Shapes,
                                            objectName = category.name,
                                            alertText = "Isso irá excluir permanentemente a categoria ${category.name} e remover todas as associações feitas à ela.",
                                            onClickButton = { viewModel.deleteCategory(category) },
                                            onDismiss = { deleteDialogIsVisible.value = false }
                                        )
                                    }
                                )

                            }

                            item {
                                if (addCategoryButton) {
                                    val value = remember { mutableStateOf("") }
                                    val isVisible = remember { mutableStateOf(false) }
                                    AddListItem(
                                        isVisible = isVisible,
                                        value = value,
                                        confirmationClick = { viewModel.addCategory(value.value, "question-mark.svg") },
                                    )
                                }

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
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(top = 10.dp)
                    ) {
                        val listState = rememberLazyListState()


                        LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {

                            items(viewModel.subCategories, key = { it.id }) { subcategory ->

                                val deleteDialogIsVisible = remember { mutableStateOf(false) }
                                view.shared.ListItem(
                                    label = subcategory.name,
                                    hasSubItem = false,
                                    spaceBetween = 0.dp,
                                    deleteDialogIsVisible = deleteDialogIsVisible,
                                    onUpdateConfirmation = { /*viewModel.updateSubcategory(subcategory, it)*/ },
                                    onContentClick = {},
                                    deleteDialog = {
                                        DialogDelete(
                                            title = "Excluir subcategoria",
                                            icon = PhosphorIcons.Light.Shapes,
                                            objectName = subcategory.name,
                                            alertText = "Isso irá excluir permanentemente a subcategoria ${subcategory.name} e remover todas as associações feitas à ela.",
                                            onClickButton = { viewModel.deleteSubcategory(subcategory) },
                                            onDismiss = { deleteDialogIsVisible.value = false }
                                        )
                                    }
                                )

                            }

                            item {
                                if (addSubcategoryButton){
                                    val value = remember { mutableStateOf("") }
                                    val isVisible = remember { mutableStateOf(false) }
                                    AddListItem(
                                        isVisible = isVisible,
                                        value = value,
                                        confirmationClick = { viewModel.addSubcategory(value.value) }
                                    )
                                }

                            }

                        }
                        VerticalScrollbar(
                            adapter = rememberScrollbarAdapter(listState),
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                }
                Divider(modifier = Modifier.width(2.dp).fillMaxHeight())
            }
        }
    }
}