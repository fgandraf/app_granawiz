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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Bold
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.bold.ArrowLeft
import com.adamglin.phosphoricons.light.Tag
import com.adamglin.phosphoricons.regular.Tag
import view.shared.*
import viewModel.TagViewModel

@Composable
fun TagsScreen(
    tagViewModel: TagViewModel = TagViewModel(),
) {

    tagViewModel.getTags()

    val tags = tagViewModel.tags.collectAsState()
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {

        //===== HEADER
        Column{
            Row(Modifier.fillMaxWidth().padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                ClickableIcon(enabled = false, icon = PhosphorIcons.Bold.ArrowLeft, iconSize = 22.dp, boxSize = 25.dp){ }
                Spacer(Modifier.width(10.dp))
                Row { AddressView(icon = PhosphorIcons.Regular.Tag, iconSize = DpSize(21.dp, 18.dp), value = "Etiquetas", rootPath = true ) }
            }
        }

        //===== BODY
        val corner = 10.dp
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .fillMaxHeight(0.85f)
                    .border(0.5.dp, MaterialTheme.colors.onSurface, shape = RoundedCornerShape(corner))
                    .clip(RoundedCornerShape(corner))
                    .background(MaterialTheme.colors.surface)
                    .padding(35.dp)
            ) {
                val listState = rememberLazyListState()
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(tags.value, key = { it.id }) { tag ->
                        val deleteDialogIsVisible = remember { mutableStateOf(false) }
                        ListItem(
                            label = tag.name,
                            icon = PhosphorIcons.Light.Tag,
                            spaceBetween = 0.dp,
                            deleteDialogIsVisible = deleteDialogIsVisible,
                            onUpdateConfirmation = { tagViewModel.updateTag(tag, it) },
                            deleteDialog = {
                                DialogDelete(
                                    title = "Excluir etiqueta",
                                    icon = PhosphorIcons.Light.Tag,
                                    objectName = tag.name,
                                    alertText = "Isso irá excluir permanentemente a etiquera ${tag.name} e remover todas as associações feitas à ela.",
                                    onClickButton = { tagViewModel.deleteTag(tag) },
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
                            confirmationClick = { tagViewModel.addTag(value.value) },
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