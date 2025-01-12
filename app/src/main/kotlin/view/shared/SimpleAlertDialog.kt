package view.shared

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Warning

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
                    Icon(
                        imageVector = PhosphorIcons.Light.Warning,
                        contentDescription = "Alert",
                        tint = MaterialTheme.colors.primary
                    )
                    Text(text = message, modifier = Modifier.padding(start = 20.dp))
                }
            },
            confirmButton = {
                DefaultButton(
                    modifier = Modifier.fillMaxWidth(),
                    confirmed = true,
                    onClick = { onDismissRequest() },
                    text = "OK"
                )
            }
        )
    
}