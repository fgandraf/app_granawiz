package view.shared

import androidx.compose.runtime.*
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.HandArrowDown
import com.adamglin.phosphoricons.light.HandArrowUp
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import domain.entity.Party
import domain.enums.PartyType
import org.jetbrains.compose.resources.stringResource
import viewModel.PartyViewModel

@Composable
fun PartyListItem(
    viewModel: PartyViewModel,
    selectedParty: Party? = null,
    item: Party,
    onClick: (Party) -> Unit,
) {
    val deleteDialogIsVisible = remember { mutableStateOf(false) }
    var updatedSuccess by mutableStateOf(true)
    var deleteSuccess by mutableStateOf(true)

    ListItem(
        label = item.name,
        hasSubItem = item.partiesNames.isNotEmpty(),
        isActive = item.id == selectedParty?.id,
        deleteDialogIsVisible = deleteDialogIsVisible,
        onUpdateConfirmation = { updatedSuccess = viewModel.updateParty(item, it) },
        onUpdateFail = {
            if (!updatedSuccess) {
                SimpleAlertDialog(
                    onDismissRequest = { viewModel.clearError(); updatedSuccess = true },
                    title = stringResource(Res.string.error_name_already_exists),
                    message = viewModel.errorMessage.value!!
                )
            }
            if (!deleteSuccess) {
                SimpleAlertDialog(
                    onDismissRequest = { viewModel.clearError(); deleteSuccess = true },
                    title = stringResource(Res.string.error_delete_party_title),
                    message = viewModel.errorMessage.value!!
                )
            }
        },
        onContentClick = {
            viewModel.selectedParty.value = item
            viewModel.getNames()
            onClick(item)
        },
        deleteDialog = {
            val type = if (viewModel.selectedType.value == PartyType.RECEIVER)
                stringResource(Res.string.party_type_receiver)
            else
                stringResource(Res.string.party_type_payer)
            val iconResource =
                if (viewModel.selectedType.value == PartyType.RECEIVER) PhosphorIcons.Light.HandArrowDown else PhosphorIcons.Light.HandArrowUp
            val hasTransactions = remember(item.id) { viewModel.hasTransactions(item) }

            if (hasTransactions) {
                val otherParties = remember(item.id) { viewModel.parties.value.filter { it.id != item.id } }
                DialogReassignAndDelete(
                    title = stringResource(Res.string.delete_party_title, type),
                    icon = iconResource,
                    party = item,
                    partyTypeName = type,
                    otherParties = otherParties,
                    onConfirm = { target ->
                        viewModel.reassignAndDelete(item, target)
                        deleteDialogIsVisible.value = false
                    },
                    onDismiss = { deleteDialogIsVisible.value = false }
                )
            } else {
                DialogDelete(
                    title = stringResource(Res.string.delete_party_title, type),
                    icon = iconResource,
                    objectName = item.name,
                    alertText = stringResource(Res.string.delete_party_confirm, type, item.name),
                    onClickButton = {
                        deleteSuccess = viewModel.deleteParty(item)
                        deleteDialogIsVisible.value = false
                    },
                    onDismiss = { deleteDialogIsVisible.value = false }
                )
            }
        }
    )
}