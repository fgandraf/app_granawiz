package ui.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ui.sidebar.enums.SidebarFooterItem


@Composable
fun SidebarFooter() {

    Row(modifier = Modifier.fillMaxWidth().height(70.dp), horizontalArrangement = Arrangement.Center) {

        var selectedItem by remember { mutableStateOf(SidebarFooterItem.WALLET) }

        SidebarFooterIcon(
            modifier = Modifier.weight(1f),
            iconResource = "icons/wallet.svg",
            isSelected = selectedItem == SidebarFooterItem.WALLET,
            onClick = { selectedItem = SidebarFooterItem.WALLET }
        )

        SidebarFooterIcon(
            modifier = Modifier.weight(1f),
            iconResource = "icons/calendar.svg",
            isSelected = selectedItem == SidebarFooterItem.CALENDAR,
            onClick = { selectedItem = SidebarFooterItem.CALENDAR }
        )

        SidebarFooterIcon(
            modifier = Modifier.weight(1f),
            iconResource = "icons/report.svg",
            isSelected = selectedItem == SidebarFooterItem.REPORT,
            onClick = { selectedItem = SidebarFooterItem.REPORT }
        )
    }
}


@Composable
fun SidebarFooterIcon(
    iconResource: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val inactiveColor = MaterialTheme.colors.primary
    val activeColor = MaterialTheme.colors.onPrimary

    val iconColor = if (isSelected) activeColor else inactiveColor
    val barColor = if (isSelected) activeColor else Color.Transparent

    Box(
        modifier = modifier
            .fillMaxHeight()
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(iconResource),
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(30.dp)
        )
        Spacer(
            Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(barColor)
                .align(Alignment.BottomCenter)
        )
    }
}