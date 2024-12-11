package viewModel

import androidx.compose.runtime.derivedStateOf
import core.entity.Party
import core.enums.PartyType
import kotlinx.coroutines.flow.MutableStateFlow
import service.PartyService

class PartyViewModel(type: PartyType) {

    val service: PartyService = PartyService()

    var parties = derivedStateOf { service.parties }
    var partyNames = derivedStateOf { service.partyNames }

    var errorMessage = derivedStateOf { service.errorMessage }

    val selectedType = MutableStateFlow(type)
    var selectedParty = MutableStateFlow(Party())
}