package view.modules.receivers.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import config.IconPaths
import model.entity.Receiver
import view.shared.ClickableIcon
import view.shared.DialogDelete
import view.theme.Afacade
import viewModel.ReceiverViewModel

@Composable
fun ReceiverListItem(
    receiverViewModel: ReceiverViewModel,
    receiver: Receiver,
){
    var value by remember { mutableStateOf(receiver.name) }
    val valueChanged = value != receiver.name
    var deleteDialog by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(8.dp))
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable {  }
    ) {
        Row {

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxHeight().padding(start = 10.dp)
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = { value = it },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 0.sp,
                        fontFamily = Afacade,
                        color = if (valueChanged) Color.Blue else MaterialTheme.colors.primary,
                    )
                )
            }
        }

        Row(modifier = Modifier.padding(end = 10.dp)) {
            if (valueChanged){
                ClickableIcon(
                    icon = "close",
                    color = Color.Blue,
                    shape = RoundedCornerShape(6.dp),
                    iconSize = 12.dp,
                    padding = true,
                    onClick = { value = receiver.name}
                )
                ClickableIcon(
                    icon = "check",
                    color = Color.Blue,
                    shape = RoundedCornerShape(6.dp),
                    iconSize = 12.dp,
                    padding = true,
                    onClick = { receiverViewModel.updateReceiver(receiver, value) }
                )
            }

            val hasSubItem by remember{ mutableStateOf(receiver.receiverNames.size > 0) }
            if (!valueChanged) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ClickableIcon(
                        icon = "trash",
                        shape = RoundedCornerShape(6.dp),
                        iconSize = 12.dp,
                        padding = true,
                        onClick = { deleteDialog = true }
                    )
                    Spacer(Modifier.width(if (hasSubItem) 0.dp else 22.dp))
                    if (hasSubItem) {
                        Spacer(Modifier.width(10.dp))
                        Icon(
                            painter = painterResource(IconPaths.SYSTEM_ICONS + "toggle_right.svg"),
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

            }


            if (deleteDialog)
                DialogDelete(
                    title = "Excluir recebedor",
                    iconResource = IconPaths.SYSTEM_ICONS + "receiver.svg",
                    objectName = receiver.name,
                    alertText = "Isso irá excluir permanentemente o recebedor ${receiver.name} e remover todas as associações feitas à ele.",
                    onClickButton = { receiverViewModel.deleteReceiver(receiver) },
                    onDismiss = { deleteDialog = false }
                )
        }

    }
    Divider(modifier = Modifier.padding(horizontal = 15.dp), thickness = 1.dp)
}