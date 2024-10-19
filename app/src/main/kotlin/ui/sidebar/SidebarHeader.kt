package ui.sidebar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.Inter

@Composable
fun SidebarHeader(totalAmount: String) {

    val primary = MaterialTheme.colors.primary
    val secondary = MaterialTheme.colors.secondary

    Row(modifier = Modifier.fillMaxWidth().height(70.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 15.dp),
        ) {
            Icon(
                painter = painterResource("icons/settings.svg"),
                contentDescription = null,
                tint = primary,
                modifier = Modifier.size(25.dp).clickable { /*TO DO()*/ },
            )
        }

        Column(modifier = Modifier.padding(start = 0.dp, top = 15.dp, end = 10.dp, bottom = 15.dp)) {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,) {
                Text(
                    text = totalAmount,
                    fontSize  = 18.sp,
                    color = secondary,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 32.sp,
                    fontFamily = Inter
                )
            }
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally,) {
                Text(
                    text = "SALDO TOTAL",
                    fontSize  = 8.sp,
                    color = primary,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 0.sp,
                    fontFamily = Inter
                )
            }
        }
    }
}