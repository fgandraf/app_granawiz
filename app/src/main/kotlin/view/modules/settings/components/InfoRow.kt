package view.modules.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import view.shared.TextNormal
import view.theme.ButtonPurple
import view.theme.DefaultFont

@Composable
fun InfoRow(
    label: String,
    value: String,
    isLink: Boolean = false,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextNormal(text = label, color = MaterialTheme.colors.primaryVariant)
        if (isLink) {
            Text(
                text = value,
                fontSize = 12.sp,
                color = ButtonPurple,
                fontWeight = FontWeight.Normal,
                fontFamily = DefaultFont,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .clickable(onClick = onClick)
                    .pointerHoverIcon(PointerIcon.Hand)
            )
        } else {
            TextNormal(text = value)
        }
    }
}
