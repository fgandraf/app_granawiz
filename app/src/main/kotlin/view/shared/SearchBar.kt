package view.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.MagnifyingGlass
import view.theme.Ubuntu

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit
) {

    var textFieldValue by remember { mutableStateOf(TextFieldValue(value)) }
    LaunchedEffect(value) {
        if (textFieldValue.text != value) {
            textFieldValue = TextFieldValue(value)
        }
    }

    Row(
        modifier = Modifier
            .height(30.dp)
            .width(320.dp)
            .border(1.dp, MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(8.dp))
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                modifier = Modifier.size(40.dp).padding(7.dp).align(Alignment.Center),
                imageVector = PhosphorIcons.Light.MagnifyingGlass,
                contentDescription = "",
                tint = MaterialTheme.colors.secondary,
            )
        }

        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = textFieldValue,
            onValueChange = { textFieldValue = it; onValueChange(it.text) },
            textStyle = TextStyle(
                color = MaterialTheme.colors.secondary,
                fontSize = 14.sp,
                fontFamily = Ubuntu,
                fontWeight = FontWeight.Medium
            ),
            decorationBox = { innerTextField ->
                if (textFieldValue.text.isEmpty()) {
                    Text(
                        text = "Pesquisar",
                        color = Color.Gray.copy(alpha = 0.5f),
                        fontSize = 14.sp,
                        lineHeight = 0.sp,
                        fontFamily = Ubuntu,
                        fontWeight = FontWeight.Normal,
                    )
                }
                innerTextField()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {}),
            singleLine = true
        )


    }
}