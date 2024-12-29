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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Shapes
import core.entity.Category
import core.entity.Subcategory
import utils.IconPaths
import view.modules.categories.components.CategoryListItem
import view.shared.AddListItem
import view.shared.DialogDelete
import view.shared.ListItem
import viewModel.CategoryViewModel


@Composable
fun CategoriesPicker(
    viewModel: CategoryViewModel = CategoryViewModel(), // Se não selecionar categorias, o problema está aqui!
    category: Category,
    subcategory: Subcategory?,
    onCategoryClick: (Category, Subcategory?) -> Unit,
) {
    viewModel.selectedCategory.value = category
    viewModel.selectedSubcategory.value = subcategory
    viewModel.getCategories(category.type)
    viewModel.getSubcategories(category)


    val categories by viewModel.categories.collectAsState()
    val subCategories by viewModel.subcategories.collectAsState()

    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedSubcategory by viewModel.selectedSubcategory.collectAsState()


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

                        items(categories, key = { it.id  }) { item ->

                            val deleteDialogIsVisible = remember { mutableStateOf(false) }

                            CategoryListItem(
                                label = item.name,
                                icon = IconPaths.CATEGORY_PACK + item.icon,
                                clickableIcon = true,
                                hasSubItem = item.subcategories.size > 0,
                                isActive =  item.id == selectedCategory.id,
                                deleteDialogIsVisible = deleteDialogIsVisible,
                                onUpdateConfirmation = { viewModel.updateCategory(category = item, name = it) },
                                onSelectIcon = { viewModel.updateCategory(category = item, icon = it) },
                                onContentClick = {
                                    viewModel.selectedCategory.value = item
                                    viewModel.selectedSubcategory.value = null
                                    viewModel.getSubcategories(item)
                                    onCategoryClick(item, null)
                                },
                                deleteDialog = {
                                    DialogDelete(
                                        title = "Excluir categoria",
                                        icon = PhosphorIcons.Light.Shapes,
                                        objectName = item.name,
                                        alertText = "Isso irá excluir permanentemente a categoria ${item.name} e remover todas as associações feitas à ela.",
                                        onClickButton = { viewModel.deleteCategory(item) },
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
                                    viewModel.addCategory(
                                        Category(
                                            type = viewModel.selectedType.value,
                                            name = value.value,
                                            icon = "question-mark.svg"
                                        )
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

                        items(subCategories, key = { it.id }) { subcategory ->

                            val deleteDialogIsVisible = remember { mutableStateOf(false) }
                            ListItem(
                                label = subcategory.name,
                                hasSubItem = false,
                                spaceBetween = 0.dp,
                                isActive = selectedSubcategory?.id == subcategory.id,
                                deleteDialogIsVisible = deleteDialogIsVisible,
                                onUpdateConfirmation = { viewModel.updateSubcategory(subcategory, it) },
                                onContentClick = {
                                    viewModel.selectedSubcategory.value = subcategory
                                    onCategoryClick(viewModel.selectedCategory.value, subcategory)
                                                 },
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
                                confirmationClick = {
                                    viewModel.addSubcategory(
                                        Subcategory(
                                            name = value.value,
                                            category = viewModel.selectedCategory.value
                                        )
                                    )
                                }
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