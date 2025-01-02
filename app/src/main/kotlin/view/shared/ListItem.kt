package view.shared

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.CaretRight
import com.adamglin.phosphoricons.light.Check
import com.adamglin.phosphoricons.light.Trash
import com.adamglin.phosphoricons.light.X
import view.modules.categories.components.DropDownIcons
import view.theme.Afacade

@Composable
fun ListItem(
    label: String = "",
    icon: ImageVector? = null,
    clickableIcon: Boolean = false,
    hasSubItem: Boolean = false,
    isActive: Boolean = false,
    spaceBetween: Dp = 22.dp,
    deleteDialogIsVisible: MutableState<Boolean> = remember { mutableStateOf(false) },
    onUpdateConfirmation: (String) -> Unit,
    onSelectIcon: (String) -> Unit = {},
    onContentClick: (() -> Unit?)? = null,
    deleteDialog: @Composable () -> Unit,
){
    var value by remember { mutableStateOf(label) }
    val valueChanged = value != label
    var expandedIcons by remember { mutableStateOf(false) }


    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
        modifier = if (onContentClick != null)
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .height(30.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isActive) Color.Cyan else Color.Transparent)
                .clickable{ onContentClick() }
                .pointerHoverIcon(PointerIcon.Hand)
        else
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .height(30.dp)
    ) {
        Row {
            if (icon != null){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = if (clickableIcon)
                        Modifier
                            .clickable { expandedIcons = true }
                            .fillMaxHeight()
                            .width(30.dp)
                    else Modifier
                        .fillMaxHeight()
                        .width(30.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (valueChanged) Color.Blue else MaterialTheme.colors.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

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
                    icon = PhosphorIcons.Light.X,
                    color = Color.Blue,
                    shape = RoundedCornerShape(6.dp),
                    onClick = { value = label },
                )
                ClickableIcon(
                    icon = PhosphorIcons.Light.Check,
                    color = Color.Blue,
                    shape = RoundedCornerShape(6.dp),
                    onClick = { onUpdateConfirmation(value) }
                )
            }

            if (!valueChanged) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ClickableIcon(
                        icon = PhosphorIcons.Light.Trash,
                        shape = RoundedCornerShape(6.dp),
                        onClick = { deleteDialogIsVisible.value = true }
                    )
                    Spacer(Modifier.width(if (hasSubItem) 0.dp else spaceBetween))
                    if (hasSubItem) {
                        Spacer(Modifier.width(10.dp))
                        Icon(
                            imageVector = PhosphorIcons.Light.CaretRight,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

            }

            if (deleteDialogIsVisible.value)
                deleteDialog()
        }
    }
    DropDownIcons(
        expanded = expandedIcons,
        onDismissRequest = { expandedIcons = false },
        onIconSelected = {onSelectIcon(it); expandedIcons = false}
    )
    Divider(modifier = Modifier.padding(horizontal = 15.dp), thickness = 1.dp)
}