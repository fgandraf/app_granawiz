package view.modules.categories

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.bold.ArrowLeft
import com.adamglin.phosphoricons.light.ChartLineUp
import com.adamglin.phosphoricons.light.Invoice
import com.adamglin.phosphoricons.light.Shapes
import com.adamglin.phosphoricons.regular.Shapes
import core.entity.Category
import core.entity.Subcategory
import core.enums.CategoryType
import utils.IconPaths
import view.modules.categories.components.CategoryListItem
import view.modules.categories.components.ListTypeItem
import view.shared.AddListItem
import view.shared.AddressView
import view.shared.ClickableIcon
import view.shared.DialogDelete
import viewModel.CategoryViewModel

@Composable
fun CategoriesScreen(
    viewModel: CategoryViewModel = CategoryViewModel(),
){

    var addCategoryButton by remember { mutableStateOf<Boolean>(false) }
    var addSubcategoryButton by remember { mutableStateOf<Boolean>(false) }

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {

        //===== HEADER
        Column{
            Row(Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                ClickableIcon(enabled = false, icon = PhosphorIcons.Bold.ArrowLeft, iconSize = 22.dp, boxSize = 25.dp){ }
                Spacer(Modifier.width(10.dp))
                Row { AddressView(icon = PhosphorIcons.Regular.Shapes, iconSize = DpSize(21.dp, 18.dp), value = "Categorias", rootPath = true ) }
            }
        }

        //===== BODY
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxHeight(0.85f)
                    .fillMaxWidth(0.85f)
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(20.dp))
                    .border(0.5.dp, MaterialTheme.colors.onSurface, shape = RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colors.surface)
                    .padding(30.dp)
            ) {
                Row {
                    //===== FIRST COLUMN
                    var activeType by remember { mutableStateOf<CategoryType?>(null) }
                    LaunchedEffect(activeType) {
                        activeType?.let { type ->
                            viewModel.selectedType.value = type
                            viewModel.getCategories(type)
                            addCategoryButton = true
                            addSubcategoryButton = false
                        }
                    }

                    Row(modifier = Modifier.weight(2f).fillMaxHeight()) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            ListTypeItem(icon = PhosphorIcons.Light.Invoice, color = MaterialTheme.colors.primary, isActive = activeType == CategoryType.EXPENSE, label = "Gastos") {
                                activeType = CategoryType.EXPENSE
                            }

                            ListTypeItem(icon = PhosphorIcons.Light.ChartLineUp, color = MaterialTheme.colors.primary, isActive = activeType == CategoryType.INCOME, label = "Rendimentos") {
                                activeType = CategoryType.INCOME
                            }
                        }
                    }
                    Divider(modifier = Modifier.width(1.dp).fillMaxHeight().background(MaterialTheme.colors.onSurface))


                    val categories by viewModel.categories.collectAsState()
                    val subCategories by viewModel.subcategories.collectAsState()

                    //===== SECOND COLUMN
                    Row(modifier = Modifier.weight(3f).fillMaxHeight()) {

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(20.dp)
                        ) {
                            val listState = rememberLazyListState()
                            var activeCategory by remember { mutableStateOf(Category()) }



                            LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {

                                items(categories, key = { it.id }) { category ->

                                    val deleteDialogIsVisible = remember { mutableStateOf(false) }

                                    CategoryListItem(
                                        label = category.name,
                                        icon = IconPaths.CATEGORY_PACK + category.icon,
                                        clickableIcon = true,
                                        hasSubItem = category.subcategories.size > 0,
                                        deleteDialogIsVisible = deleteDialogIsVisible,
                                        isActive = activeCategory == category,
                                        onUpdateConfirmation = { viewModel.updateCategory(category = category, name = it) },
                                        onSelectIcon = { viewModel.updateCategory(category = category, icon = it) },
                                        onContentClick = {
                                            activeCategory = category
                                            viewModel.getSubcategories(category)
                                            viewModel.selectedCategory.value = category
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

                            }
                            VerticalScrollbar(
                                adapter = rememberScrollbarAdapter(listState),
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                    }
                    Divider(modifier = Modifier.width(1.dp).fillMaxHeight().background(MaterialTheme.colors.onSurface))


                    //===== THIRD COLUMN
                    Row(modifier = Modifier.weight(3f).fillMaxHeight()) {

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(20.dp)
                        ) {
                            val listState = rememberLazyListState()


                            LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {

                                items(subCategories, key = { it.id }) { subcategory ->

                                    val deleteDialogIsVisible = remember { mutableStateOf(false) }
                                    view.shared.ListItem(
                                        label = subcategory.name,
                                        hasSubItem = false,
                                        spaceBetween = 0.dp,
                                        deleteDialogIsVisible = deleteDialogIsVisible,
                                        onUpdateConfirmation = { viewModel.updateSubcategory(subcategory, it) },
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

                            }
                            VerticalScrollbar(
                                adapter = rememberScrollbarAdapter(listState),
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                    }
                    Divider(modifier = Modifier.width(1.dp).fillMaxHeight().background(MaterialTheme.colors.onSurface))
                }
            }
        }
    }
}