package view.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Faders
import com.adamglin.phosphoricons.light.MagnifyingGlass
import view.theme.Ubuntu

@Composable
fun SearchBar(
    onTuneClicked: () -> Unit,
    onSearchClicked: () -> Unit
) {

    var text by remember { mutableStateOf(TextFieldValue()) }

    Row(modifier = Modifier
        .height(35.dp)
        .width(360.dp)
        .border(1.dp, MaterialTheme.colors.primaryVariant, shape = RoundedCornerShape(8.dp))
        .background(MaterialTheme.colors.onPrimary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                .size(40.dp)
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable { onTuneClicked() },
        ) {
            Icon(
                modifier = Modifier.fillMaxSize().padding(10.dp).align(Alignment.Center),
                imageVector = PhosphorIcons.Light.Faders,
                contentDescription = "",
                tint = MaterialTheme.colors.secondary,
            )
        }

        BasicTextField(
            modifier = Modifier
                .weight(5f)
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            value = text,
            onValueChange = { text = it },
            textStyle = TextStyle(
                color = MaterialTheme.colors.secondary,
                fontSize = 14.sp,
                fontFamily = Ubuntu,
                fontWeight = FontWeight.Medium
            ),
            decorationBox = { innerTextField ->
                if (text.text.isEmpty()) {
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
            keyboardActions = KeyboardActions(onSearch = { onSearchClicked() }),
            singleLine = true
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .size(40.dp)
                .clip(RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable { onSearchClicked() },
        ) {
            Icon(
                modifier = Modifier.size(40.dp).padding(10.dp).align(Alignment.Center),
                imageVector = PhosphorIcons.Light.MagnifyingGlass,
                contentDescription = "",
                tint = MaterialTheme.colors.secondary,
            )
        }
    }
}