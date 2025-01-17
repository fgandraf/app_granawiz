package view.shared

import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.HandArrowUp
import core.entity.PartyName
import viewModel.PartyViewModel

@Composable
fun PartyNameListItem(
    viewModel: PartyViewModel,
    item: PartyName,
) {
    val deleteDialogIsVisible = remember { mutableStateOf(false) }
    var updatedSuccess by mutableStateOf(true)

    ListItem(
        label = item.name,
        hasSubItem = false,
        isActive = viewModel.selectedName.value?.id == item.id,
        spaceBetween = 0.dp,
        deleteDialogIsVisible = deleteDialogIsVisible,
        onUpdateConfirmation = { updatedSuccess = viewModel.updateName(item, it) },
        onUpdateFail = {
            if (!updatedSuccess) {
                SimpleAlertDialog(
                    onDismissRequest = { viewModel.clearError(); updatedSuccess = true },
                    title = "Associação já existente",
                    message = viewModel.errorMessage.value!!
                )
            }
        },
        onContentClick = null,
        deleteDialog = {
            DialogDelete(
                title = "Excluir nome",
                icon = PhosphorIcons.Light.HandArrowUp,
                objectName = item.name,
                alertText = "Isso irá excluir permanentemente o nome ${item.name} e remover todas as associações feitas à ele.",
                onClickButton = { viewModel.deleteName(item) },
                onDismiss = { deleteDialogIsVisible.value = false }
            )
        }
    )


}