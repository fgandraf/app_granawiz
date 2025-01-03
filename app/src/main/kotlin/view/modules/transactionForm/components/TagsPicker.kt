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
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Tag
import core.entity.Tag
import view.shared.AddListItem
import view.shared.DialogDelete
import view.shared.ListItem
import viewModel.TagViewModel


@Composable
fun TagsPicker(
    viewModel: TagViewModel = remember { TagViewModel()},
    selected: List<Tag>,
    onTagClick: (List<Tag>) -> Unit
) {

    viewModel.loadTags()
    viewModel.selectedTags.value = selected

    val tags = viewModel.tags.collectAsState()
    val selectedTags by viewModel.selectedTags.collectAsState()

    val corner = 30.dp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onPrimary, RoundedCornerShape(topEnd = corner, bottomEnd = corner))
            .border(1.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(topEnd = corner, bottomEnd = corner))
    ) {


        Box(
            modifier = Modifier.weight(1f).fillMaxHeight().padding(vertical = 30.dp, horizontal = 10.dp),
        ) {
            val listState = rememberLazyListState()


            LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {

                items(tags.value, key = { it.id  }) { tag ->

                    val deleteDialogIsVisible = remember { mutableStateOf(false) }

                    ListItem(
                        label = tag.name,
                        isActive = selectedTags.any { it.id == tag.id },
                        icon = PhosphorIcons.Light.Tag,
                        spaceBetween = 0.dp,
                        deleteDialogIsVisible = deleteDialogIsVisible,
                        onUpdateConfirmation = { viewModel.updateTag(tag, it) },
                        onContentClick = {
                            viewModel.toggleTagSelection(tag)
                            onTagClick(viewModel.selectedTags.value)
                        },
                        deleteDialog = {
                            DialogDelete(
                                title = "Excluir etiqueta",
                                icon = PhosphorIcons.Light.Tag,
                                objectName = tag.name,
                                alertText = "Isso irá excluir permanentemente a etiqueta ${tag.name} e remover todas as associações feitas à ela.",
                                onClickButton = { viewModel.deleteTag(tag) },
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
                        confirmationClick = { viewModel.addTag(value.value) },
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