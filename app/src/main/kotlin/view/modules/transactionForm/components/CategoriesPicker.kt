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
import core.enums.CategoryType
import utils.IconPaths
import view.modules.categories.components.CategoryListItem
import view.shared.AddListItem
import view.shared.DialogDelete
import view.shared.ListItem
import viewModel.CategoryViewModel


@Composable
fun CategoriesPicker(
    viewModel: CategoryViewModel = CategoryViewModel(),
    category: Category,
    subcategory: Subcategory?,
    type: CategoryType? = null,
    onCategoryClick: (Category, Subcategory?) -> Unit,
) {

    viewModel.selectedCategory.value = category
    viewModel.selectedSubcategory.value = subcategory
    viewModel.selectedType.value = if (category.id == 0L) type!! else category.type

    viewModel.getCategories(viewModel.selectedType.value)
    viewModel.getSubcategories(category)

    val categories by viewModel.categories.collectAsState()
    val subCategories by viewModel.subcategories.collectAsState()


    // EXTERNAL
    val corner = 10.dp
    Box(
        modifier = Modifier
            .width(600.dp)
            .background(MaterialTheme.colors.onPrimary, RoundedCornerShape(topEnd = corner, bottomEnd = corner))
            .border(1.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(topEnd = corner, bottomEnd = corner)),
        contentAlignment = Alignment.Center
    ) {

        // CONTENT BOX
        Row(modifier = Modifier.padding(vertical = 30.dp, horizontal = 10.dp)) {

            // CATEGORIES
            Box(modifier = Modifier.weight(1f)){
                val listState = rememberLazyListState()
                LazyColumn(state = listState) {
                    items(categories, key = { it.id  }) { item -> AddCategoryItem(viewModel, item) { onCategoryClick(it, null) } }
                    item { AddCategoryButton(viewModel) }
                }
                VerticalScrollbar(adapter = rememberScrollbarAdapter(listState), modifier = Modifier.align(Alignment.CenterEnd))
            }

            // DIVIDER
            Divider(modifier = Modifier.width(2.dp).fillMaxHeight())

            // SUBCATEGORIES
            Box(modifier = Modifier.weight(1f)){
                val listState = rememberLazyListState()
                LazyColumn(state = listState) {
                    items(subCategories, key = { it.id  }) { item -> AddSubcategoryItem(viewModel, item) { onCategoryClick(viewModel.selectedCategory.value, it) } }
                    item { AddSubcategoryButton(viewModel) }
                }
                VerticalScrollbar(adapter = rememberScrollbarAdapter(listState),modifier = Modifier.align(Alignment.CenterEnd))
            }

        }

    }

}



@Composable
fun AddCategoryItem(
    viewModel: CategoryViewModel,
    item: Category,
    onClick: (Category) -> Unit
){
    val deleteDialogIsVisible = remember { mutableStateOf(false) }
    CategoryListItem(
        label = item.name,
        icon = IconPaths.CATEGORY_PACK + item.icon,
        clickableIcon = true,
        hasSubItem = item.subcategories.size > 0,
        isActive =  item.id == viewModel.selectedCategory.value.id,
        deleteDialogIsVisible = deleteDialogIsVisible,
        onUpdateConfirmation = { viewModel.updateCategory(category = item, name = it) },
        onSelectIcon = { viewModel.updateCategory(category = item, icon = it) },
        onContentClick = {
            viewModel.selectedCategory.value = item
            viewModel.selectedSubcategory.value = null
            viewModel.getSubcategories(item)
            onClick(item)
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

@Composable
fun AddCategoryButton(viewModel: CategoryViewModel){
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

@Composable
fun AddSubcategoryItem(
    viewModel: CategoryViewModel,
    item: Subcategory,
    onClick: (Subcategory) -> Unit
){
    val deleteDialogIsVisible = remember { mutableStateOf(false) }
    ListItem(
        label = item.name,
        hasSubItem = false,
        spaceBetween = 0.dp,
        isActive = viewModel.selectedSubcategory.value?.id == item.id,
        deleteDialogIsVisible = deleteDialogIsVisible,
        onUpdateConfirmation = { viewModel.updateSubcategory(item, it) },
        onContentClick = {
            viewModel.selectedSubcategory.value = item
            onClick(item)
        },
        deleteDialog = {
            DialogDelete(
                title = "Excluir subcategoria",
                icon = PhosphorIcons.Light.Shapes,
                objectName = item.name,
                alertText = "Isso irá excluir permanentemente a subcategoria ${item.name} e remover todas as associações feitas à ela.",
                onClickButton = { viewModel.deleteSubcategory(item) },
                onDismiss = { deleteDialogIsVisible.value = false }
            )
        }
    )
}

@Composable
fun AddSubcategoryButton(viewModel: CategoryViewModel){
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