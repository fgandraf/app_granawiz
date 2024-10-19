package ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DashboardScreen() {
    Row(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colors.background)
    )
    { }
}