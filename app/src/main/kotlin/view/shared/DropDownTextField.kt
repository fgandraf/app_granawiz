package view.shared

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import config.IconPaths

@Composable
fun DropDownTextField(
    modifier: Modifier = Modifier,
    categoryIcon: String? = null,
    boxSize: Dp = 35.dp,
    textAlign: TextAlign = TextAlign.Center,
    value: String,
    label: String? = null,
    placeholder: String = "",
    onClick: () -> Unit
){
    val primaryColor = MaterialTheme.colors.primary
    val secondaryColor = MaterialTheme.colors.secondary

    var borderSize by remember { mutableStateOf(1.dp) }
    var borderColor by remember { mutableStateOf(primaryColor) }

    Column(modifier = modifier) {
        if (label != null)
            TextPrimary(text = label, modifier = Modifier.padding(bottom = 5.dp), size = 10.sp)

        Box(contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(boxSize)
                .border(borderSize, borderColor, shape = RoundedCornerShape(5.dp))
                .clip(RoundedCornerShape(5.dp))
                .pointerHoverIcon(PointerIcon.Hand)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused){ borderSize = 1.2.dp; borderColor = secondaryColor }
                    else { borderSize = 1.dp; borderColor = primaryColor }
                }
                .clickable { onClick() }
        ) {
            if (value.isEmpty())
                TextPrimary(
                    text = placeholder,
                    size = 10.sp,
                    align = textAlign,
                    color = primaryColor.copy(alpha = 0.75f),
                    modifier = Modifier.fillMaxWidth())
            else
            {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(IconPaths.CATEGORY_PACK + categoryIcon),
                        contentDescription = "Category Icon",
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.size(15.dp)
                    )

                    TextPrimary(text = value, modifier = Modifier.padding(start = 5.dp))
                }
            }


            Icon(
                painter = painterResource(IconPaths.SYSTEM_ICONS + "toggle_right.svg"),
                contentDescription = "click",
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 10.dp)
            )
        }
    }
}