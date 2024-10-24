package ui.categories.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.Afacade

@Composable
fun ListItem(
    icon: String? = null,
    color: Color = MaterialTheme.colors.primary,
    label: String,
    hasSubItems: Boolean = false,
    onIconClick: () -> Unit = {},
    onContainerClick: () -> Unit = {},
    onTrailingClick: () -> Unit = {}

){
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(8.dp))
            .pointerHoverIcon(if (icon != null) PointerIcon.Hand else PointerIcon.Default)
            .clickable(enabled = icon != null) { onContainerClick() }
    ) {
        Row {
            if (icon != null){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(40.dp)
                        .clickable {  onIconClick() }
                ) {
                    Icon(
                        painter = painterResource("icons/pack/$icon"),
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxHeight().padding(start = if (icon == null) 20.dp else 0.dp)
            ) {
                Text(
                    text = label,
                    fontSize  = 14.sp,
                    color = color,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 0.sp,
                    fontFamily = Afacade,
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Text).clickable{ onTrailingClick() }
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .width(40.dp)
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable(enabled = !hasSubItems) { onTrailingClick() }
        ) {
            val trailingIcon = if (hasSubItems) "icons/system/toggle_right.svg" else "icons/system/trash.svg"
            Icon(
                painter = painterResource(trailingIcon),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(12.dp)
            )
        }
    }
    Divider(modifier = Modifier.padding(horizontal = 15.dp), thickness = 1.dp)
}