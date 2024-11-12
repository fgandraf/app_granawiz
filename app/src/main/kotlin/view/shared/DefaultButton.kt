package view.shared

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import view.theme.Purple600
import view.theme.Ubuntu

@Composable
fun DefaultButton(
    confirmed: Boolean,
    label: String,
    color: Color = Purple600,
    onClick: () -> Unit,
){
    Button(
        enabled = confirmed,
        colors = ButtonDefaults.buttonColors(backgroundColor = color),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .pointerHoverIcon(if(confirmed) PointerIcon.Hand else PointerIcon.Default),
    ){
        Text(
            text = label,
            color = Color.White,
            fontStyle = if(!confirmed) FontStyle.Italic else FontStyle.Normal,
            fontFamily = Ubuntu,
            fontSize = 12.sp,
        )
    }
}