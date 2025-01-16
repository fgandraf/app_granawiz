package view.shared

import androidx.compose.runtime.*
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.HandArrowDown
import com.adamglin.phosphoricons.light.HandArrowUp
import core.entity.Party
import core.enums.PartyType
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

    ListItem(
        label = item.name,
        hasSubItem = item.partiesNames.size > 0,
        isActive = item.id == selectedParty?.id,
        deleteDialogIsVisible = deleteDialogIsVisible,
        onUpdateConfirmation = { updatedSuccess = viewModel.updateParty(item, it) },
        onUpdateFail = {
            if (!updatedSuccess) {
                SimpleAlertDialog(
                    onDismissRequest = { viewModel.clearError(); updatedSuccess = true },
                    title = "Nome já existente",
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
            val type = if (viewModel.selectedType.value == PartyType.RECEIVER) "recebedor" else "pagador"
            val iconResource =
                if (viewModel.selectedType.value == PartyType.RECEIVER) PhosphorIcons.Light.HandArrowDown else PhosphorIcons.Light.HandArrowUp

            DialogDelete(
                title = "Excluir $type",
                icon = iconResource,
                objectName = item.name,
                alertText = "Isso irá excluir permanentemente o $type ${item.name} e remover todas as associações feitas à ele.",
                onClickButton = { viewModel.deleteParty(item) },
                onDismiss = { deleteDialogIsVisible.value = false }
            )
        }
    )
}