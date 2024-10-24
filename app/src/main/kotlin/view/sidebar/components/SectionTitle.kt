package view.sidebar.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.theme.Gray600
import ui.theme.Ubuntu

@Composable
fun SectionTitle(label: String) {
    Text(
        text = label,
        fontSize = 10.sp, fontFamily = Ubuntu,
        fontWeight = FontWeight.Medium, color = Gray600,
        modifier = Modifier.offset(x = 15.dp).height(30.dp)
    )
}