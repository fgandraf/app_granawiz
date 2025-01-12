package view.shared

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Warning
import view.theme.RedWarning

@Composable
fun SimpleQuestionDialog(
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit,
    title: String = "",
    message: String,
    surfaceColor: Color = MaterialTheme.colors.surface
){
    AlertDialog(
        modifier = Modifier.width(400.dp),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = surfaceColor,
        onDismissRequest = { onDismissRequest() },
        title = { Text(title) },
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = PhosphorIcons.Light.Warning,
                    contentDescription = "Alert",
                    tint = MaterialTheme.colors.primary
                )
                TextNormal(text = message, modifier = Modifier.padding(start = 15.dp))
            }
        },
        dismissButton = {
            DefaultButton(
                color = MaterialTheme.colors.secondaryVariant,
                text = "Não",
                textPadding = 20.dp,
                onClick = onDismissRequest
            )
        },
        confirmButton = {
            DefaultButton(
                modifier = Modifier.padding(start = 5.dp),
                color = RedWarning,
                textColor = Color.White,
                textPadding = 20.dp,
                text = "Sim",
                onClick = onConfirmRequest,
            )
        }

    )
    
}