package view.modules.importStatement.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
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
import com.adamglin.phosphoricons.light.CaretDown
import com.adamglin.phosphoricons.light.CaretUp
import domain.entity.Party
import view.shared.TextNormal
import view.theme.DefaultFont

@Composable
fun SearchablePartyField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = "",
    options: List<Party>,
    showBorder: Boolean = true,
    borderOnActive: Boolean = false,
    maxLength: Int = Int.MAX_VALUE,
    onPartySelected: (Party) -> Unit,
    onFreeText: (String) -> Unit,
) {
    val primaryColor = MaterialTheme.colors.primary
    val secondaryColor = MaterialTheme.colors.secondary
    val surfaceColor = MaterialTheme.colors.surface
    val density = LocalDensity.current

    val focusRequester = remember { FocusRequester() }
    var text by remember(value) { mutableStateOf(value) }
    var expanded by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(-1) }
    var fieldSize by remember { mutableStateOf(IntSize.Zero) }
    var filterByText by remember { mutableStateOf(false) }
    var togglePressedWhileExpanded by remember { mutableStateOf(false) }

    val isActive = isFocused || expanded
    val borderSize = if (isActive) 1.2.dp else 1.dp
    val borderColor = if (isActive) secondaryColor else primaryColor
    val effectiveShowBorder = if (borderOnActive) isActive else showBorder
    val filtered = remember(text, options, filterByText) {
        if (!filterByText || text.isBlank()) options
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
            modifier = boxModifier.padding(start = 10.dp)
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { fs ->
                        isFocused = fs.isFocused
                        if (!fs.isFocused) {
                            if (!togglePressedWhileExpanded) expanded = false
                            selectedIndex = -1
                            onFreeText(text)
                        }
                    }
                    .onPreviewKeyEvent { event ->
                        if (event.type != KeyEventType.KeyDown) return@onPreviewKeyEvent false
                        when (event.key) {
                            Key.DirectionDown -> {
                                if (!expanded) { expanded = true; return@onPreviewKeyEvent true }
                                selectedIndex = (selectedIndex + 1).coerceAtMost(filtered.size - 1)
                                true
                            }
                            Key.DirectionUp -> {
                                selectedIndex = (selectedIndex - 1).coerceAtLeast(0)
                                true
                            }
                            Key.Enter -> {
                                if (selectedIndex >= 0 && selectedIndex < filtered.size) {
                                    val party = filtered[selectedIndex]
                                    text = party.name
                                    onPartySelected(party)
                                    expanded = false
                                    selectedIndex = -1
                                    true
                                } else false
                            }
                            Key.Escape -> {
                                expanded = false
                                selectedIndex = -1
                                true
                            }
                            else -> false
                        }
                    },
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = DefaultFont,
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.primary,
                ),
                value = text,
                onValueChange = { newText ->
                    if (newText.length <= maxLength) {
                        text = newText
                        selectedIndex = -1
                        filterByText = true
                        expanded = true
                    }
                },
                decorationBox = { innerTextField ->
                    if (text.isEmpty())
                        Text(
                            text = placeholder,
                            style = TextStyle(
                                fontFamily = DefaultFont,
                                fontSize = 12.sp,
                                color = primaryColor,
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    innerTextField()
                }
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(30.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp))
                    .align(Alignment.CenterEnd)
                    .pointerHoverIcon(PointerIcon.Hand)
                    .pointerInput(Unit) {
                        detectTapGestures(onPress = {
                            togglePressedWhileExpanded = expanded
                            if (tryAwaitRelease()) {
                                filterByText = false
                                expanded = !togglePressedWhileExpanded
                                if (!togglePressedWhileExpanded) focusRequester.requestFocus()
                            }
                            togglePressedWhileExpanded = false
                        })
                    }
            ) {
                Icon(
                    imageVector = if (expanded) PhosphorIcons.Light.CaretUp else PhosphorIcons.Light.CaretDown,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp),
                    tint = MaterialTheme.colors.primary
                )
            }
        }

        if (filtered.isNotEmpty()) {
            val transitionState = remember { MutableTransitionState(false) }
            transitionState.targetState = expanded

            if (transitionState.currentState || transitionState.targetState) {
                val popupWidth = with(density) { fieldSize.width.toDp() }.coerceAtLeast(160.dp)
                val offsetY = with(density) { 35.dp.roundToPx() }

                Popup(
                    alignment = Alignment.TopStart,
                    offset = IntOffset(x = 0, y = offsetY),
                    onDismissRequest = { expanded = false },
                    properties = PopupProperties(focusable = false, dismissOnClickOutside = false),
                ) {
                    AnimatedVisibility(
                        visibleState = transitionState,
                        enter = fadeIn(tween(150)),
                        exit = fadeOut(tween(10)),
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
                                        .background(
                                            if (index == selectedIndex) primaryColor.copy(alpha = 0.08f)
                                            else surfaceColor
                                        )
                                        .pointerHoverIcon(PointerIcon.Hand)
                                        .clickable {
                                            text = party.name
                                            onPartySelected(party)
                                            expanded = false
                                            selectedIndex = -1
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
    }
}
