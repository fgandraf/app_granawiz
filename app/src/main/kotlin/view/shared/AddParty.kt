package view.shared

import androidx.compose.runtime.*
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
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
                    title = stringResource(Res.string.error_name_already_exists),
                    message = viewModel.errorMessage.value!!
                )
            }
        }
    )
}