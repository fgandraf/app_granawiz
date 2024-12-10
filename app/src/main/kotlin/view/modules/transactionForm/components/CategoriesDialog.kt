package view.modules.transactionForm.components

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
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Shapes
import core.entity.Subcategory
import utils.IconPaths
import view.modules.categories.components.CategoryListItem
import view.shared.AddListItem
import view.shared.DialogDelete
import view.shared.ListItem
import viewModel.TransactionFormViewModel


@Composable
fun CategoriesDialog(viewModel: TransactionFormViewModel) {

    viewModel.loadCategories()
    val corner = 30.dp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onPrimary, RoundedCornerShape(topEnd = corner, bottomEnd = corner))
            .border(1.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(topEnd = corner, bottomEnd = corner))
    ) {

        Row(modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp)) {

            //===== CATEGORIES
            Row(modifier = Modifier.width(260.dp).fillMaxHeight()) {

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
                                isActive = viewModel.category.id == category.id,
                                deleteDialogIsVisible = deleteDialogIsVisible,
                                onUpdateConfirmation = { viewModel.updateCategory(category, it, category.icon) },
                                onSelectIcon = { viewModel.updateCategory(category, category.name, it) },
                                onContentClick = {
                                    if (viewModel.category.id != category.id) {
                                        viewModel.category = category
                                        viewModel.subCategory = Subcategory()
                                    }

                                    viewModel.loadSubCategories(category)
                                    viewModel.selectCategory(category)

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
                            val value = remember { mutableStateOf("") }
                            val isVisible = remember { mutableStateOf(false) }
                            AddListItem(
                                isVisible = isVisible,
                                value = value,
                                confirmationClick = { viewModel.addCategory(value.value, "question-mark.svg") },
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

                        items(viewModel.subCategories, key = { it.id }) { subcategory ->

                            val deleteDialogIsVisible = remember { mutableStateOf(false) }
                            ListItem(
                                label = subcategory.name,
                                hasSubItem = false,
                                spaceBetween = 0.dp,
                                isActive = viewModel.subCategory?.id == subcategory.id,
                                deleteDialogIsVisible = deleteDialogIsVisible,
                                onUpdateConfirmation = { /*viewModel.updateSubcategory(subcategory, it)*/ },
                                onContentClick = { viewModel.subCategory = subcategory; Unit },
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
                            val value = remember { mutableStateOf("") }
                            val isVisible = remember { mutableStateOf(false) }
                            AddListItem(
                                isVisible = isVisible,
                                value = value,
                                confirmationClick = { viewModel.addSubcategory(value.value) }
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