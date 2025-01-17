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
    viewModel: TagViewModel = remember { TagViewModel() },
    selected: List<Tag>,
    onTagClick: (List<Tag>) -> Unit,
) {

    viewModel.getTags()
    viewModel.selectedTags.value = selected

    val tags = viewModel.tags.collectAsState()


    // EXTERNAL
    val corner = 10.dp
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface, RoundedCornerShape(topEnd = corner, bottomEnd = corner))
            .border(0.5.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(topEnd = corner, bottomEnd = corner)),
        contentAlignment = Alignment.Center
    ) {

        // TAGS
        Box(modifier = Modifier.padding(vertical = 30.dp, horizontal = 10.dp)) {
            val listState = rememberLazyListState()
            LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                items(tags.value, key = { it.id }) { item ->

                    AddTagItem(viewModel, item) { onTagClick(it) }
                }
                item { AddTagButton(viewModel) }
            }
            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(listState),
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

    }

}

@Composable
fun AddTagItem(
    viewModel: TagViewModel,
    item: Tag,
    onClick: (List<Tag>) -> Unit,
) {
    val deleteDialogIsVisible = remember { mutableStateOf(false) }
    ListItem(
        label = item.name,
        isActive = viewModel.selectedTags.value.any { it.id == item.id },
        icon = PhosphorIcons.Light.Tag,
        spaceBetween = 0.dp,
        deleteDialogIsVisible = deleteDialogIsVisible,
        onUpdateConfirmation = { viewModel.updateTag(item, it) },
        onContentClick = { viewModel.toggleTagSelection(item); onClick(viewModel.selectedTags.value) },
        deleteDialog = {
            DialogDelete(
                title = "Excluir etiqueta",
                icon = PhosphorIcons.Light.Tag,
                objectName = item.name,
                alertText = "Isso irá excluir permanentemente a etiqueta ${item.name} e remover todas as associações feitas à ela.",
                onClickButton = { viewModel.deleteTag(item) },
                onDismiss = { deleteDialogIsVisible.value = false }
            )
        }
    )
}

@Composable
fun AddTagButton(viewModel: TagViewModel) {
    val value = remember { mutableStateOf("") }
    val isVisible = remember { mutableStateOf(false) }
    AddListItem(
        isVisible = isVisible,
        value = value,
        confirmationClick = { viewModel.addTag(value.value) },
    )
}