package ui.sidebar

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.sidebar.components.ButtonFooterItem
import ui.sidebar.components.FooterItem
import ui.sidebar.enums.SidebarFooterItem

@Composable
fun SidebarFooter() {


    Column {

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 15.dp)
        ) {
            ButtonFooterItem("icons/add_group.svg")
            ButtonFooterItem("icons/add_account.svg")
            ButtonFooterItem("icons/arrange.svg")
        }

        Divider()

        Row(modifier = Modifier.fillMaxWidth().height(70.dp), horizontalArrangement = Arrangement.Center) {

            var selectedItem by remember { mutableStateOf(SidebarFooterItem.WALLET) }

            FooterItem(
                modifier = Modifier.weight(1f),
                iconResource = "icons/wallet.svg",
                isSelected = selectedItem == SidebarFooterItem.WALLET,
                onClick = { selectedItem = SidebarFooterItem.WALLET }
            )

            FooterItem(
                modifier = Modifier.weight(1f),
                iconResource = "icons/calendar.svg",
                isSelected = selectedItem == SidebarFooterItem.CALENDAR,
                onClick = { selectedItem = SidebarFooterItem.CALENDAR }
            )

            FooterItem(
                modifier = Modifier.weight(1f),
                iconResource = "icons/report.svg",
                isSelected = selectedItem == SidebarFooterItem.REPORT,
                onClick = { selectedItem = SidebarFooterItem.REPORT }
            )
        }

    }


}