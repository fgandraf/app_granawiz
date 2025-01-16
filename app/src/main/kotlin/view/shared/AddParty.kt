package view.shared

import androidx.compose.runtime.*
import viewModel.PartyViewModel

@Composable
fun AddParty(viewModel: PartyViewModel) {
    val value = remember { mutableStateOf("") }
    val isVisible = remember { mutableStateOf(false) }
    var success by mutableStateOf(true)
    AddListItem(
        isVisible = isVisible,
        value = value,
        confirmationClick = { success = viewModel.addParty(name = value.value) },
        alertDialogContent = {
            if (!success) {
                SimpleAlertDialog(
                    onDismissRequest = { viewModel.clearError(); success = true },
                    title = "Nome já existente",
                    message = viewModel.errorMessage.value!!
                )
            }
        }
    )
}