package ui.sidebar.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.AppFont

@Composable
fun GroupMenuItem(
    label: String,
    color: Color,
    value: String,
    onClick: () -> Unit
){

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable { onClick() }
    ) {

        Row(modifier = Modifier.padding(start = 20.dp).align(Alignment.CenterStart)) {
            Icon(
                painter = painterResource("icons/toggle_down.svg"),
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .size(15.dp),
            )
        }

        Row(modifier = Modifier.padding(start = 45.dp).align(Alignment.CenterStart)) {
            Text(modifier = Modifier.padding(0.dp),
                text = label,
                fontSize  = 14.sp,
                color = color,
                fontWeight = FontWeight.Medium,
                lineHeight = 0.sp,
                fontFamily = AppFont
            )
        }

        Row(modifier = Modifier.padding(start = 20.dp).align(Alignment.CenterStart)) {
            Text(modifier = Modifier.padding(0.dp),
                text = value,
                fontSize  = 14.sp,
                color = Color.Green,
                fontWeight = FontWeight.Medium,
                lineHeight = 0.sp,
                fontFamily = AppFont
            )
        }
    }
}