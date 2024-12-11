package view.modules.tags

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Tag
import view.shared.*
import viewModel.TagViewModel

@Composable
fun TagsScreen(
    tagViewModel: TagViewModel = TagViewModel(),
) {


    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {

        //===== HEADER
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row { AddressView(icon = PhosphorIcons.Light.Tag, value = "Etiquetas") }
            }
        }

        //===== BODY
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .fillMaxHeight(0.85f)
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(20.dp))
                    .border(0.5.dp, MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colors.onPrimary)
                    .padding(30.dp)
            ) {
                val listState = rememberLazyListState()
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(tagViewModel.tags.value, key = { it.id }) { tag ->
                        val deleteDialogIsVisible = remember { mutableStateOf(false) }
                        ListItem(
                            label = tag.name,
                            icon = PhosphorIcons.Light.Tag,
                            spaceBetween = 0.dp,
                            deleteDialogIsVisible = deleteDialogIsVisible,
                            onUpdateConfirmation = { tagViewModel.service.updateTag(tag, it) },
                            onContentClick = {},
                            deleteDialog = {
                                DialogDelete(
                                    title = "Excluir etiqueta",
                                    icon = PhosphorIcons.Light.Tag,
                                    objectName = tag.name,
                                    alertText = "Isso irá excluir permanentemente a etiquera ${tag.name} e remover todas as associações feitas à ela.",
                                    onClickButton = { tagViewModel.service.deleteTag(tag) },
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
                            icon = PhosphorIcons.Light.Tag,
                            confirmationClick = { tagViewModel.service.addTag(value.value) },
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