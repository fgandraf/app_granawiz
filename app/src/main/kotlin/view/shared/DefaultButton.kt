package view.shared

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import view.theme.Purple600
import view.theme.Ubuntu

@Composable
fun DefaultButton(
    confirmed: Boolean,
    label: String,
    onClick: () -> Unit,
){
    Button(
        modifier = Modifier.fillMaxWidth().pointerHoverIcon(if(confirmed) PointerIcon.Hand else PointerIcon.Default),
        enabled = confirmed,
        colors = ButtonDefaults.buttonColors(backgroundColor = Purple600),
        onClick = onClick
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