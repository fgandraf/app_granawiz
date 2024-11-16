package view.modules.tags

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import config.IconPaths
import view.modules.categories.components.AddButton
import view.modules.tags.components.NewTagItem
import view.modules.tags.components.TagListItem
import view.shared.AddressView
import view.shared.SearchBar
import viewModel.TagViewModel

@Composable
fun TagsScreen(
    tagViewModel: TagViewModel = TagViewModel(),
) {
    var renameVisible by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {

        //===== HEADER
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row { AddressView(IconPaths.SYSTEM_ICONS + "tag.svg", "Etiquetas") }
                SearchBar(onTuneClicked = { /* TO DO */ }, onSearchClicked = { /* TO DO */ })
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
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(tagViewModel.tags, key = { it.id }) { tag ->
                        TagListItem(tagViewModel, tag)
                    }
                    item {
                        Column {
                            val label = remember { mutableStateOf("") }
                            AnimatedVisibility(visible = renameVisible) {
                                NewTagItem(
                                    tagViewModel = tagViewModel,
                                    label = label,
                                    onValueChange = { label.value = it },
                                    onDismiss = { renameVisible = false; label.value = "" }
                                )
                            }
                            AnimatedVisibility(visible = !renameVisible) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .padding(end = 10.dp),
                                    horizontalAlignment = Alignment.End,
                                    verticalArrangement = Arrangement.Bottom
                                ) {
                                    Row(
                                        modifier = Modifier.width(130.dp),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        AddButton { renameVisible = true }
                                    }
                                }
                            }
                        }
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