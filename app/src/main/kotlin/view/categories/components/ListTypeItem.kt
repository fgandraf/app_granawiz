package view.categories.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
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
fun ListTypeItem(
    icon: String,
    color: Color,
    label: String,
    onClick: () -> Unit
){
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .height(30.dp)
            .clip(RoundedCornerShape(8.dp))
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable { onClick() }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 15.dp)) {
            Icon(
                painter = painterResource("assets/icons/systemIcons/$icon"),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(15.dp)
            )

            Text(
                text = label,
                fontSize = 14.sp,
                color = color,
                fontWeight = FontWeight.Medium,
                lineHeight = 0.sp,
                fontFamily = Afacade,
                modifier = Modifier.padding(start = 15.dp)
            )
        }
        Icon(
            painter = painterResource("assets/icons/systemIcons/toggle_right.svg"),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(12.dp).offset(x = (-10).dp)
        )
    }
    Divider(modifier = Modifier.padding(horizontal = 15.dp), thickness = 1.dp)
}