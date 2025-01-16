package view.modules.party.components

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import core.enums.PartyType
import view.shared.*
import viewModel.PartyViewModel

@Composable
fun Body(
    partyType: PartyType,
    viewModel: PartyViewModel = PartyViewModel(partyType),
) {

    viewModel.getParties()

    val parties by viewModel.parties.collectAsState()
    val names by viewModel.partyNames.collectAsState()

    val selectedParty by viewModel.selectedParty.collectAsState()


    // EXTERNAL
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        // CONTENT BOX
        val corner = 10.dp
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight(0.85f)
                .fillMaxWidth(0.75f)
                .border(0.5.dp, MaterialTheme.colors.onSurface, shape = RoundedCornerShape(corner))
                .clip(RoundedCornerShape(corner))
                .background(MaterialTheme.colors.surface)
        ) {



            // PARTIES
            Box(modifier = Modifier.weight(1f).fillMaxHeight().padding(35.dp)) {
                val listState = rememberLazyListState()
                LazyColumn(state = listState, modifier = Modifier.fillMaxSize().padding(top = 30.dp)) {
                    items(parties, key = { it.id }) { item -> PartyListItem(viewModel, selectedParty, item) {} }
                    item { AddParty(viewModel) }
                }
                VerticalScrollbar(adapter = rememberScrollbarAdapter(listState), modifier = Modifier.align(Alignment.CenterEnd))
            }


            // DIVIDER
            Divider(modifier = Modifier.width(1.dp).fillMaxHeight(0.95f).background(MaterialTheme.colors.onSurface))


            // PARTYNAMES
            Box(modifier = Modifier.weight(1f).fillMaxHeight().padding(35.dp)) {
                TextNormal(text = "Nomes associados:")
                val listState = rememberLazyListState()
                LazyColumn(state = listState, modifier = Modifier.fillMaxSize().padding(top = 30.dp)) {
                    items(names, key = { it.id }) { item -> PartyNameListItem(viewModel, item)}
                    item { AddPartyName(viewModel) }
                }
                VerticalScrollbar(adapter = rememberScrollbarAdapter(listState), modifier = Modifier.align(Alignment.CenterEnd))
            }

        }
    }
}