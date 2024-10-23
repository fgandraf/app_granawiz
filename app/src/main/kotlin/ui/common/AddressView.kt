package ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import ui.theme.Afacade

@Composable
fun AddressView(icon: String, value: String) {
    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(modifier = Modifier.padding(horizontal = 10.dp),
            text = "/",
            fontSize  = 14.sp,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Medium,
            lineHeight = 0.sp,
            fontFamily = Afacade
        )
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.size(13.dp),
        )
        Text(modifier = Modifier.padding(horizontal = 5.dp),
            text = value,
            fontSize  = 14.sp,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Medium,
            lineHeight = 0.sp,
            fontFamily = Afacade
        )
    }
}