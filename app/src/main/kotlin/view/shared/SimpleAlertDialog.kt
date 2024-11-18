package view.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import config.IconPaths

@Composable
fun SimpleAlertDialog(
    onDismissRequest: () -> Unit,
    title: String,
    message: String,
){
        AlertDialog(
            modifier = Modifier.width(400.dp),
            onDismissRequest = { onDismissRequest() },
            title = { Text(title) },
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(IconPaths.SYSTEM_ICONS + "alert.svg"),
                        contentDescription = "Alert"
                    )
                    Text(text = message, modifier = Modifier.padding(start = 20.dp))
                }
            },
            confirmButton = {
                DefaultButton(
                    confirmed = true,
                    onClick = { onDismissRequest() },
                    label = "OK"
                )
            }
        )
    
}