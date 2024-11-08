package view.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DialogTitleBar(title: String, onCloseRequest: () -> Unit) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(5.dp).padding(start = 5.dp)
    ) {
        TextPrimary(
            text = title,
            size = 12.sp,
            color = MaterialTheme.colors.secondary,
        )
        ClickableIcon(
            icon = "close",
            padding = true,
            color = MaterialTheme.colors.secondary,
            onClick = onCloseRequest
        )
    }

}