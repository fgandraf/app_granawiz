package view.modules.party

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import config.IconPaths
import core.enums.PartyType
import view.modules.party.components.Body
import view.shared.AddressView
import view.shared.SearchBar
import viewModel.PartyViewModel

@Composable
fun PayersScreen(
    viewModel: PartyViewModel = PartyViewModel(PartyType.PAYER)
) {

    viewModel.loadParties()

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {

        //===== HEADER
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row { AddressView(IconPaths.SYSTEM_ICONS + "payer.svg", "Pagadores") }
                SearchBar(onTuneClicked = { /* TO DO */ }, onSearchClicked = { /* TO DO */ })
            }
        }

        //===== BODY
        Body(viewModel)

    }
}