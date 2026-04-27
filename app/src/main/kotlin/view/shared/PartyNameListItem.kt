package view.shared

import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.HandArrowUp
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import domain.entity.PartyName
import org.jetbrains.compose.resources.stringResource
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
                    title = stringResource(Res.string.error_association_already_exists),
                    message = viewModel.errorMessage.value!!
                )
            }
        },
        onContentClick = null,
        deleteDialog = {
            DialogDelete(
                title = stringResource(Res.string.delete_party_name_title),
                icon = PhosphorIcons.Light.HandArrowUp,
                objectName = item.name,
                alertText = stringResource(Res.string.delete_party_name_confirm, item.name),
                onClickButton = { viewModel.deleteName(item) },
                onDismiss = { deleteDialogIsVisible.value = false }
            )
        }
    )


}