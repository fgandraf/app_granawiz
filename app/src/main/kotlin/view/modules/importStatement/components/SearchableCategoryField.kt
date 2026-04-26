package view.modules.importStatement.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.*
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
import com.adamglin.phosphoricons.light.CaretDown
import com.adamglin.phosphoricons.light.CaretUp
import domain.entity.Category
import domain.entity.Subcategory
import utils.IconPaths
import utils.rememberSvgPainter
import view.shared.TextNormal
import view.theme.Ubuntu

private sealed class CategoryItem {
    data class Parent(val category: Category) : CategoryItem()
    data class Sub(val subcategory: Subcategory) : CategoryItem()
}

// label shown in the dropdown list
private val CategoryItem.dropdownLabel: String
    get() = when (this) {
        is CategoryItem.Parent -> category.name
        is CategoryItem.Sub -> subcategory.name
    }

// text placed in the field when the item is selected
private val CategoryItem.selectedText: String
    get() = when (this) {
        is CategoryItem.Parent -> category.name
        is CategoryItem.Sub -> "${subcategory.category.name}/${subcategory.name}"
    }

private val CategoryItem.iconPath: String
    get() = when (this) {
        is CategoryItem.Parent -> category.icon
        is CategoryItem.Sub -> ""
    }

@Composable
fun SearchableCategoryField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String = "",
    options: List<Category>,
    showBorder: Boolean = true,
    borderOnActive: Boolean = false,
    onCategorySelected: (Category, Subcategory?) -> Unit,
) {
    val primaryColor = MaterialTheme.colors.primary
    val secondaryColor = MaterialTheme.colors.secondary
    val surfaceColor = MaterialTheme.colors.surface
    val density = LocalDensity.current

    var text by remember { mutableStateOf(value) }
    var expanded by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(-1) }
    var fieldSize by remember { mutableStateOf(IntSize.Zero) }
    var filterByText by remember { mutableStateOf(false) }

    val isActive = isFocused || expanded
    val borderSize = if (isActive) 1.2.dp else 1.dp
    val borderColor = if (isActive) secondaryColor else primaryColor
    val effectiveShowBorder = if (borderOnActive) isActive else showBorder

    val allItems = remember(options) {
        options.flatMap { cat ->
            buildList {
                add(CategoryItem.Parent(cat))
                cat.subcategories.forEach { sub -> add(CategoryItem.Sub(sub)) }
            }
        }
    }

    val filtered = remember(text, allItems, filterByText) {
        if (!filterByText || text.isBlank()) allItems
        else allItems.filter { item ->
            when (item) {
                is CategoryItem.Parent -> item.category.name.contains(text, ignoreCase = true)
                is CategoryItem.Sub ->
                    item.subcategory.name.contains(text, ignoreCase = true) ||
                    item.subcategory.category.name.contains(text, ignoreCase = true)
            }
        }
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
                    .onFocusChanged { fs ->
                        isFocused = fs.isFocused
                        if (!fs.isFocused) {
                            expanded = false
                            selectedIndex = -1
                            if (allItems.none { it.selectedText.equals(text, ignoreCase = true) }) {
                                text = value
                            }
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
                                    selectItem(filtered[selectedIndex], onCategorySelected) { display ->
                                        text = display
                                    }
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
                    fontFamily = Ubuntu,
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.primary,
                ),
                value = text,
                onValueChange = { newText ->
                    text = newText
                    selectedIndex = -1
                    filterByText = true
                    expanded = true
                },
                decorationBox = { innerTextField ->
                    if (text.isEmpty())
                        Text(
                            text = placeholder,
                            style = TextStyle(
                                fontFamily = Ubuntu,
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
                    .clickable {
                        filterByText = false
                        expanded = !expanded
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
                    exit = fadeOut(tween(150)),
                ) {
                    Column(
                        modifier = Modifier
                            .width(popupWidth)
                            .shadow(6.dp, RoundedCornerShape(4.dp))
                            .background(surfaceColor, RoundedCornerShape(4.dp))
                            .border(0.5.dp, primaryColor.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                    ) {
                        if (filtered.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                TextNormal(
                                    text = "Nenhuma encontrada",
                                    color = primaryColor.copy(alpha = 0.5f)
                                )
                            }
                        } else {
                            val scrollState = rememberScrollState()
                            Box(modifier = Modifier.heightIn(max = 240.dp)) {
                                Column(modifier = Modifier.verticalScroll(scrollState)) {
                                    filtered.forEachIndexed { index, item ->
                                        val isSub = item is CategoryItem.Sub
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(
                                                    if (index == selectedIndex) primaryColor.copy(alpha = 0.08f)
                                                    else surfaceColor
                                                )
                                                .pointerHoverIcon(PointerIcon.Hand)
                                                .clickable {
                                                    selectItem(item, onCategorySelected) { display -> text = display }
                                                    expanded = false
                                                    selectedIndex = -1
                                                }
                                                .padding(
                                                    start = if (isSub) 35.dp else 12.dp,
                                                    end = 12.dp,
                                                    top = 8.dp,
                                                    bottom = 8.dp
                                                ),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            if (!isSub && item.iconPath.isNotBlank()) {
                                                Icon(
                                                    painter = rememberSvgPainter(IconPaths.CATEGORY_PACK + item.iconPath),
                                                    contentDescription = null,
                                                    tint = primaryColor,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                            TextNormal(
                                                text = item.dropdownLabel,
                                                color = if (isSub) primaryColor.copy(alpha = 0.75f) else primaryColor
                                            )
                                        }
                                        if (index < filtered.lastIndex) {
                                            Divider(color = primaryColor.copy(alpha = 0.08f))
                                        }
                                    }
                                }
                                VerticalScrollbar(
                                    adapter = rememberScrollbarAdapter(scrollState),
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun selectItem(
    item: CategoryItem,
    onCategorySelected: (Category, Subcategory?) -> Unit,
    setText: (String) -> Unit,
) {
    when (item) {
        is CategoryItem.Parent -> {
            setText(item.selectedText)
            onCategorySelected(item.category, null)
        }
        is CategoryItem.Sub -> {
            setText(item.selectedText)
            onCategorySelected(item.subcategory.category, item.subcategory)
        }
    }
}
