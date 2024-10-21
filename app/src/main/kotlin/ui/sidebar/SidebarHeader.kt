package ui.sidebar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.AppFont

@Composable
fun SidebarHeader(totalAmount: String) {

    val primary = MaterialTheme.colors.primary
    val secondary = MaterialTheme.colors.secondary

    Box(modifier = Modifier.fillMaxWidth().height(70.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 15.dp)
                .align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource("icons/settings.svg"),
                contentDescription = null,
                tint = primary,
                modifier = Modifier.pointerHoverIcon(PointerIcon.Hand).size(25.dp).clickable { /*TO DO()*/ },
            )
        }

        Column(modifier = Modifier.padding(vertical = 15.dp)) {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,) {
                Text(
                    text = totalAmount,
                    fontSize  = 18.sp,
                    color = secondary,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 32.sp,
                    fontFamily = AppFont
                )
            }
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,) {
                Text(
                    text = "SALDO TOTAL",
                    fontSize  = 8.sp,
                    color = primary,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 0.sp,
                    fontFamily = AppFont
                )
            }
        }
    }
}