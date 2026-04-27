package view.modules.accountForm.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Fill
import com.adamglin.phosphoricons.fill.PencilLine
import utils.isCustomIcon
import utils.parseCustomIcon
import utils.rememberAccountIconPainter
import view.shared.*
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

@Composable
fun IconSelector(
    icon: String,
    iconSvg: String? = null,
    onIconSelected: (icon: String, iconSvg: String?) -> Unit,
) {
    var expandedIcons by remember { mutableStateOf(false) }
    var pendingFile by remember { mutableStateOf<File?>(null) }
    var customIconName by remember { mutableStateOf("") }
    var customIconSuggestion by remember { mutableStateOf("") }
    var showNameDialog by remember { mutableStateOf(false) }

    if (showNameDialog && pendingFile != null) {
        val dismiss = {
            showNameDialog = false
            pendingFile = null
            customIconName = ""
            customIconSuggestion = ""
        }
        Dialog(onDismissRequest = dismiss) {
            Column(
                Modifier
                    .width(320.dp)
                    .background(MaterialTheme.colors.surface, RoundedCornerShape(8.dp))
            ) {
                DialogTitleBar(title = "Ícone personalizado", onCloseRequest = dismiss)
                Divider(Modifier.background(MaterialTheme.colors.onSurface))

                Column(Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    TextSmall(
                        text = "Digite um nome para identificar este ícone.",
                        color = MaterialTheme.colors.primary.copy(alpha = 0.7f)
                    )
                    Spacer(Modifier.height(12.dp))
                    DefaultTextField(
                        value = customIconName,
                        onValueChange = { customIconName = it },
                        label = "Nome",
                        placeholder = customIconSuggestion,
                    )
                }

                Divider(Modifier.background(MaterialTheme.colors.onSurface))

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TransparentButton(modifier = Modifier.width(100.dp), text = "Cancelar", onClick = dismiss)
                    DefaultButton(
                        confirmed = customIconName.isNotBlank() || customIconSuggestion.isNotBlank(),
                        text = "Confirmar",
                        textColor = Color.White,
                        onClick = {
                            val svgContent = pendingFile!!.readText()
                            val finalName = customIconName.trim().ifBlank { customIconSuggestion }
                            onIconSelected("CUSTOM|$finalName", svgContent)
                            dismiss()
                        }
                    )
                }
            }
        }
    }

    val painter = rememberAccountIconPainter(icon, iconSvg)
    val customDisplayName = if (isCustomIcon(icon)) parseCustomIcon(icon).first else null
    val iconTint = if (iconSvg != null) Color.Unspecified else MaterialTheme.colors.primaryVariant

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .background(MaterialTheme.colors.surface, CircleShape)
                .clip(CircleShape)
                .border(0.8.dp, MaterialTheme.colors.primaryVariant, CircleShape)
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable { expandedIcons = !expandedIcons }
        ) {
            Box(modifier = Modifier.padding(15.dp)) {
                Icon(
                    modifier = Modifier.size(60.dp),
                    painter = painter,
                    contentDescription = null,
                    tint = iconTint
                )
                Icon(
                    modifier = Modifier.size(14.dp).align(Alignment.BottomEnd),
                    imageVector = PhosphorIcons.Fill.PencilLine,
                    contentDescription = "",
                    tint = MaterialTheme.colors.primary
                )
            }
            DropDownIcons(
                expanded = expandedIcons,
                onDismissRequest = { expandedIcons = false },
                onIconSelected = { fileName -> onIconSelected(fileName, null) },
                onCustomIconRequest = {
                    val chooser = JFileChooser().apply {
                        dialogTitle = "Selecionar arquivo SVG"
                        isAcceptAllFileFilterUsed = false
                        fileFilter = FileNameExtensionFilter("Imagens SVG (*.svg)", "svg")
                    }
                    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        pendingFile = chooser.selectedFile
                        customIconName = ""
                        customIconSuggestion = chooser.selectedFile.nameWithoutExtension
                        showNameDialog = true
                    }
                }
            )
        }
        if (customDisplayName != null) {
            Spacer(Modifier.height(6.dp))
            TextSmall(text = customDisplayName)
        }
    }
}
