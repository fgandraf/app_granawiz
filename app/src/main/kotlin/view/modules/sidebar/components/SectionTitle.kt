package view.modules.sidebar.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import view.theme.Gray600
import view.theme.Ubuntu

@Composable
fun SectionTitle(label: String) {
    Text(
        text = label,
        fontSize = 10.sp, fontFamily = Ubuntu,
        fontWeight = FontWeight.Medium, color = Gray600,
        modifier = Modifier.height(40.dp).padding(top = 15.dp, start = 15.dp)
    )
}