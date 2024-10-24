package view.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun DashboardScreen() {
    Row(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically)
    {
        Text(text = "Dashboard")
    }
}