package view.shared

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
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
fun DeleteScheduleDialog(
    onDismiss: () -> Unit,
    onDeleteThis: () -> Unit,
    onDeleteThisAndFuture: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier.width(420.dp),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
        onDismissRequest = onDismiss,
        title = null,
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = PhosphorIcons.Light.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                )
                TextNormal(
                    text = "Como deseja excluir este agendamento?",
                    modifier = Modifier.padding(start = 15.dp),
                )
            }
        },
        buttons = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                DefaultButton(
                    modifier = Modifier.fillMaxWidth(),
                    color = RedWarning,
                    textColor = Color.White,
                    text = "Excluir somente este",
                    onClick = onDeleteThis,
                )
                DefaultButton(
                    modifier = Modifier.fillMaxWidth(),
                    color = RedWarning,
                    textColor = Color.White,
                    text = "Excluir este e todos os futuros",
                    onClick = onDeleteThisAndFuture,
                )
                DefaultButton(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colors.secondaryVariant,
                    text = "Cancelar",
                    onClick = onDismiss,
                )
            }
        },
    )
}
