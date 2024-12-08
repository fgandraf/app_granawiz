package view.modules.transactionForm.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.CaretRight
import view.shared.TextPrimary
import view.theme.Ubuntu

@Composable
fun DefaultPartyField(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textAlign: TextAlign = TextAlign.Start,
    value: String,
    label: String = "",
    placeholder: String = "",
    onValueChange: (String) -> Unit
){
    val primaryColor = MaterialTheme.colors.primary
    val secondaryColor = MaterialTheme.colors.secondary

    var borderSize by remember { mutableStateOf(1.dp) }
    var borderColor by remember { mutableStateOf(primaryColor) }

    Column(modifier = modifier) {

        TextPrimary(text = label, modifier = Modifier.padding(bottom = 5.dp), size = 10.sp)

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .height(35.dp)
                .fillMaxWidth()
                .border(borderSize, borderColor, shape = RoundedCornerShape(5.dp))
                .clip(RoundedCornerShape(5.dp))
                .padding(start = 10.dp)

        ) {
            Row(modifier = Modifier.weight(1f)) {
                BasicTextField(
                    enabled = enabled,
                    modifier = Modifier.weight(1f)
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                borderSize = 1.2.dp; borderColor = secondaryColor
                            } else {
                                borderSize = 1.dp; borderColor = primaryColor
                            }
                        },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontFamily = Ubuntu,
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.primary,
                        lineHeight = 16.sp,
                        textAlign = textAlign
                    ),
                    value = value,
                    onValueChange = onValueChange,
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier) {
                            if (value.isEmpty())
                                TextPrimary(
                                    text = placeholder,
                                    size = 10.sp,
                                    align = textAlign,
                                    color = primaryColor.copy(alpha = 0.75f)
                                )
                        }
                        innerTextField()
                    }
                )
            }


            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                .size(35.dp)
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable { }
            ) {
                Icon(
                    imageVector = PhosphorIcons.Light.CaretRight,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp)
                )
            }


        }
    }
}