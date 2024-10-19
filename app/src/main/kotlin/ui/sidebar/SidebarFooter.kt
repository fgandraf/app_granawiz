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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SidebarFooter() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(70.dp),
        horizontalArrangement = Arrangement.Center
    )
    {

        val unselected = MaterialTheme.colors.primary
        val selected = MaterialTheme.colors.onPrimary



        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxHeight().fillMaxWidth(0.33f)
        ) {

            Box(modifier = Modifier.fillMaxSize()) {

                Icon(
                    painter = painterResource("icons/wallet.svg"),
                    contentDescription = null,
                    tint = selected,
                    modifier = Modifier.size(30.dp).align(Alignment.Center).clickable { /*TO DO()*/ },
                )

                Spacer(Modifier.fillMaxWidth().height(6.dp).background(selected).align(Alignment.BottomCenter))
            }
        }



        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxHeight().fillMaxWidth(0.5f)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                Icon(
                    painter = painterResource("icons/calendar.svg"),
                    contentDescription = null,
                    tint = unselected,
                    modifier = Modifier.size(30.dp).align(Alignment.Center).clickable { /*TO DO()*/ },
                )

                Spacer(Modifier.fillMaxWidth().height(6.dp).background(Color.Transparent).align(Alignment.BottomCenter))
            }
        }



        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxHeight().fillMaxWidth(1f)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                Icon(
                    painter = painterResource("icons/report.svg"),
                    contentDescription = null,
                    tint = unselected,
                    modifier = Modifier.size(30.dp).align(Alignment.Center).clickable { /*TO DO()*/ },
                )

                Spacer(Modifier.fillMaxWidth().height(6.dp).background(Color.Transparent).align(Alignment.BottomCenter))
            }
        }

    }
}