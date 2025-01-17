package view.shared

import androidx.compose.runtime.*
import viewModel.PartyViewModel

@Composable
fun AddPartyName(viewModel: PartyViewModel) {
    val value = remember { mutableStateOf("") }
    val isVisible = remember { mutableStateOf(false) }
    var success by mutableStateOf(true)
    AddListItem(
        isVisible = isVisible,
        value = value,
        confirmationClick = { success = viewModel.addName(value.value) },
        alertDialogContent = {
            if (!success) {
                SimpleAlertDialog(
                    onDismissRequest = { viewModel.clearError(); success = true },
                    title = "Associação já existente",
                    message = viewModel.errorMessage.value!!
                )
            }
        }
    )
}