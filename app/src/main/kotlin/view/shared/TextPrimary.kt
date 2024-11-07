package view.shared

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import view.theme.Ubuntu

@Composable
fun TextPrimary(
    modifier: Modifier = Modifier,
    text: String,
    color : Color = MaterialTheme.colors.primary,
    weight: FontWeight = FontWeight.Normal,
    size: TextUnit = 12.sp,
    align: TextAlign = TextAlign.Start,
    lineHeight: TextUnit = 12.sp,
    italic: Boolean = false
) {
    Text(
        modifier = modifier,
        text = text,
        fontSize = size,
        color = color,
        fontWeight = weight,
        lineHeight = lineHeight,
        fontFamily = Ubuntu,
        textAlign = align,
        fontStyle = if(italic) FontStyle.Italic else FontStyle.Normal
    )
}