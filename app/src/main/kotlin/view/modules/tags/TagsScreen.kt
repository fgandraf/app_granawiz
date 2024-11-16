package view.modules.tags

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxHeight(0.85f)
                    .fillMaxWidth(0.40f)
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(20.dp))
                    .border(0.5.dp, MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colors.onPrimary)
            ) {
                val scrollState = rememberScrollState()
                Box(modifier = Modifier.fillMaxSize().padding(30.dp)){

                    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier) {

                        Column(
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .verticalScroll(scrollState)
                                .padding(end = 12.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().padding(top = 35.dp)) {

                                tagViewModel.tags.forEach { tag -> TagListItem(tagViewModel, tag) }

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
                                    Column(modifier = Modifier.fillMaxWidth().height(40.dp).padding(end = 10.dp), horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Bottom) {
                                        Row(modifier = Modifier.width(130.dp), horizontalArrangement = Arrangement.Center) {
                                            AddButton { renameVisible = true }
                                        }
                                    }
                                }

                            }
                        }
                    }

                    VerticalScrollbar(
                        adapter = rememberScrollbarAdapter(scrollState),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .fillMaxHeight()
                            .padding(vertical = 3.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.primaryVariant.copy(alpha = 0.3F))
                    )
                }
            }
        }
    }
}