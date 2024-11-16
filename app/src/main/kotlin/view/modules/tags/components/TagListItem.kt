package view.modules.tags.components

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import config.IconPaths
import model.entity.Tag
import view.shared.ClickableIcon
import view.shared.DialogDelete
import view.theme.Afacade
import viewModel.TagViewModel

@Composable
fun TagListItem(
    tagViewModel: TagViewModel,
    tag: Tag,
){
    var value by remember { mutableStateOf(tag.name) }
    val valueChanged = value != tag.name
    var deleteDialog by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp).height(30.dp).clip(RoundedCornerShape(8.dp))
    ) {
        Row {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight().width(40.dp)
            ) {
                Icon(
                    painter = painterResource(IconPaths.SYSTEM_ICONS + "tag.svg"),
                    contentDescription = null,
                    tint = if (valueChanged) Color.Blue else MaterialTheme.colors.primary,
                    modifier = Modifier.size(15.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxHeight()
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
                    onClick = { value = tag.name}
                )
                ClickableIcon(
                    icon = "check",
                    color = Color.Blue,
                    shape = RoundedCornerShape(6.dp),
                    iconSize = 12.dp,
                    padding = true,
                    onClick = { tagViewModel.updateTag(tag, value) }
                )
            }
            else
                ClickableIcon(
                    icon = "trash",
                    shape = RoundedCornerShape(6.dp),
                    iconSize = 12.dp,
                    padding = true,
                    onClick = { deleteDialog = true }
                )

            if (deleteDialog)
                DialogDelete(
                    title = "Excluir etiqueta",
                    iconResource = IconPaths.SYSTEM_ICONS + "tag.svg",
                    objectName = tag.name,
                    alertText = "Isso irá excluir permanentemente a etiqueta ${tag.name} e remover todas as associações feitas à ela.",
                    onClickButton = { tagViewModel.deleteTag(tag) },
                    onDismiss = { deleteDialog = false }
                )
        }

    }
    Divider(modifier = Modifier.padding(horizontal = 15.dp), thickness = 1.dp)
}