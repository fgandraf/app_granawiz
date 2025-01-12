package view.modules.sidebar.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import view.shared.TextSmall

@Composable
fun SectionTitle(label: String) {
    TextSmall(
        modifier = Modifier.height(40.dp).padding(top = 15.dp, start = 15.dp),
        text = label,
        color = MaterialTheme.colors.primaryVariant
    )
}