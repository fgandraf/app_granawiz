package view.modules.importStatement.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.Receipt
import com.adamglin.phosphoricons.regular.UploadSimple
import view.shared.FocusableBox
import view.shared.TextNormal
import view.shared.TextSmall
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.FilenameFilter

@Composable
fun FilePickerField(
    modifier: Modifier = Modifier,
    label: String? = null,
    selectedFile: File?,
    onFileSelected: (File) -> Unit,
) {
    Column(modifier = modifier) {
        if (label != null)
            TextSmall(text = label, modifier = Modifier.padding(bottom = 5.dp))

        FocusableBox(onClick = {
            val dialog = FileDialog(null as Frame?, "Selecionar extrato bancário", FileDialog.LOAD)
            dialog.filenameFilter = FilenameFilter { _, name ->
                name.lowercase().endsWith(".ofx")
            }
            dialog.isVisible = true
            val dir = dialog.directory
            val name = dialog.file
            dialog.dispose()
            if (dir != null && name != null) {
                onFileSelected(File(dir, name))
            }
        }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = PhosphorIcons.Regular.Receipt,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(8.dp))
                if (selectedFile == null)
                    TextNormal(
                        text = "Clique para selecionar arquivo",
                        color = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                        modifier = Modifier.weight(1f)
                    )
                else
                    TextNormal(text = selectedFile.name, modifier = Modifier.weight(1f))
                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector = PhosphorIcons.Regular.UploadSimple,
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
