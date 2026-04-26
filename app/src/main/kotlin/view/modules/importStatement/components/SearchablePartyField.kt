package view.modules.importStatement.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.CaretRight
import domain.entity.Party
import view.shared.TextNormal
import view.shared.TextSmall
import view.theme.Ubuntu

@Composable
fun SearchablePartyField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = "",
    options: List<Party>,
    showBorder: Boolean = true,
    borderOnActive: Boolean = false,
    onPartySelected: (Party) -> Unit,
    onFreeText: (String) -> Unit,
) {
    val primaryColor = MaterialTheme.colors.primary
    val secondaryColor = MaterialTheme.colors.secondary
    val surfaceColor = MaterialTheme.colors.surface
    val density = LocalDensity.current

    var text by remember { mutableStateOf(value) }
    var expanded by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }
    var borderSize by remember { mutableStateOf(1.dp) }
    var borderColor by remember { mutableStateOf(primaryColor) }
    var fieldSize by remember { mutableStateOf(IntSize.Zero) }

    val effectiveShowBorder = if (borderOnActive) isFocused else showBorder
    val filtered = remember(text, options) {
        if (text.isBlank()) options
        else options.filter { it.name.contains(text, ignoreCase = true) }
    }

    val boxModifier = if (effectiveShowBorder)
        Modifier.fillMaxWidth().height(35.dp)
            .border(borderSize, borderColor, shape = RoundedCornerShape(5.dp))
            .clip(RoundedCornerShape(5.dp))
    else
        Modifier.fillMaxWidth().height(35.dp)

    Box(modifier = modifier.onSizeChanged { fieldSize = it }) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = boxModifier.padding(horizontal = 10.dp)
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp)
                    .onFocusChanged { fs ->
                        isFocused = fs.isFocused
                        if (fs.isFocused) {
                            borderSize = 1.2.dp
                            borderColor = secondaryColor
                            expanded = true
                        } else {
                            borderSize = 1.dp
                            borderColor = primaryColor
                            expanded = false
                            onFreeText(text)
                        }
                    },
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = Ubuntu,
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.primary,
                ),
                value = text,
                onValueChange = { newText ->
                    text = newText
                    expanded = true
                },
                decorationBox = { innerTextField ->
                    if (text.isEmpty())
                        TextSmall(
                            text = placeholder,
                            color = primaryColor.copy(alpha = 0.75f),
                            modifier = Modifier.fillMaxWidth()
                        )
                    innerTextField()
                }
            )

            Icon(
                imageVector = PhosphorIcons.Light.CaretRight,
                contentDescription = null,
                modifier = Modifier.size(15.dp).align(Alignment.CenterEnd),
                tint = MaterialTheme.colors.primary
            )
        }

        if (expanded && filtered.isNotEmpty()) {
            val popupWidth = with(density) { fieldSize.width.toDp() }.coerceAtLeast(160.dp)
            val offsetY = with(density) { 35.dp.roundToPx() }

            Popup(
                alignment = Alignment.TopStart,
                offset = IntOffset(x = 0, y = offsetY),
                onDismissRequest = { expanded = false },
                properties = PopupProperties(focusable = false, dismissOnClickOutside = false),
            ) {
                Column(
                    modifier = Modifier
                        .width(popupWidth)
                        .shadow(6.dp, RoundedCornerShape(4.dp))
                        .background(surfaceColor, RoundedCornerShape(4.dp))
                        .border(0.5.dp, primaryColor.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                ) {
                    filtered.take(8).forEachIndexed { index, party ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .pointerHoverIcon(PointerIcon.Hand)
                                .clickable {
                                    text = party.name
                                    onPartySelected(party)
                                    expanded = false
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            TextNormal(text = party.name)
                        }
                        if (index < filtered.lastIndex) {
                            Divider(color = primaryColor.copy(alpha = 0.08f))
                        }
                    }
                }
            }
        }
    }
}
