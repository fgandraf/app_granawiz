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
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.light.Tag
import com.adamglin.phosphoricons.regular.Tag
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.delete_tag_confirm
import com.felipegandra.generated.resources.delete_tag_title
import com.felipegandra.generated.resources.nav_tags
import domain.structs.PageAddress
import org.jetbrains.compose.resources.stringResource
import view.shared.*
import viewModel.TagViewModel

@Composable
fun TagsScreen(
    tagViewModel: TagViewModel = TagViewModel(),
) {

    tagViewModel.getTags()

    val tags = tagViewModel.tags.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 15.dp, bottom = 15.dp)
            .border(1.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colors.surface)
    ) {

        //===== HEADER
        DefaultScreenHeader(
            addresses = listOf(
                PageAddress(
                    iconVector = PhosphorIcons.Regular.Tag,
                    iconSize = DpSize(21.dp, 18.dp),
                    name = stringResource(Res.string.nav_tags),
                    rootPath = true
                )
            )
        )

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
                    .background(MaterialTheme.colors.background.copy(0.6f))
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
                                    title = stringResource(Res.string.delete_tag_title),
                                    icon = PhosphorIcons.Light.Tag,
                                    objectName = tag.name,
                                    alertText = stringResource(Res.string.delete_tag_confirm, tag.name),
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