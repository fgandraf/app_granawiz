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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.entity.Party
import core.entity.PartyName
import core.enums.PartyType
import view.shared.*
import viewModel.PartyViewModel


@Composable
fun PartiesPicker(
    partyType: PartyType,
    viewModel: PartyViewModel = remember { PartyViewModel(type = partyType) },
    party: Party? = null,
    partyName: PartyName? = null,
    onPartyClick: (Party) -> Unit
) {
    viewModel.selectedParty.value = party
    viewModel.selectedName.value = partyName
    viewModel.getParties()
    viewModel.getNames()

    val parties by viewModel.parties.collectAsState()
    val names by viewModel.partyNames.collectAsState()

    // EXTERNAL
    val corner = 10.dp
    Box(
        modifier = Modifier
            .width(600.dp)
            .background(MaterialTheme.colors.surface, RoundedCornerShape(topEnd = corner, bottomEnd = corner))
            .border(0.5.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(topEnd = corner, bottomEnd = corner)),
        contentAlignment = Alignment.Center
    ) {

        // CONTENT BOX
        Row(modifier = Modifier.padding(vertical = 30.dp, horizontal = 10.dp)) {

            // PARTIES
            Box(modifier = Modifier.weight(1f)){
                val listState = rememberLazyListState()
                LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                    items(parties, key = { it.id  }) { item -> PartyListItem(viewModel, viewModel.selectedParty.value, item) { onPartyClick(it) } }
                    item { AddParty(viewModel) }
                }
                VerticalScrollbar(adapter = rememberScrollbarAdapter(listState), modifier = Modifier.align(Alignment.CenterEnd))
            }

            // DIVIDER
            Divider(modifier = Modifier.width(1.dp).fillMaxHeight().background(MaterialTheme.colors.onSurface))

            // PARTYNAMES
            Box(modifier = Modifier.weight(1f)){
                val listState = rememberLazyListState()
                LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                    item { Spacer(Modifier.height(10.dp)) }
                    item { TextNormal(modifier = Modifier.padding(start = 20.dp, bottom = 10.dp), text = "Nomes associados:") }
                    items(names, key = { it.id }) { item -> PartyNameListItem(viewModel, item) }
                    item { AddPartyName(viewModel) }
                }
                VerticalScrollbar(adapter = rememberScrollbarAdapter(listState), modifier = Modifier.align(Alignment.CenterEnd))
            }

        }

    }

}






