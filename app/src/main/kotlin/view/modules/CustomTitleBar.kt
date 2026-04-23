package view.modules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.ArrowsIn
import com.adamglin.phosphoricons.light.ArrowsOut
import com.adamglin.phosphoricons.light.Minus
import com.adamglin.phosphoricons.light.X
import view.shared.TextH3

@Composable
fun FrameWindowScope.CustomTitleBar(
    windowState: WindowState,
    onCloseRequest: () -> Unit
) {
    Column {
        WindowDraggableArea {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .background(MaterialTheme.colors.surface)
                    .pointerInput(windowState.placement) {
                        var lastClickMs = 0L
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent(PointerEventPass.Main)
                                val pressed = event.changes.any { it.pressed && !it.previousPressed }
                                if (pressed) {
                                    val now = System.currentTimeMillis()
                                    if (now - lastClickMs < 300L) {
                                        windowState.placement =
                                            if (windowState.placement == WindowPlacement.Maximized)
                                                WindowPlacement.Floating
                                            else
                                                WindowPlacement.Maximized
                                        lastClickMs = 0L
                                    } else {
                                        lastClickMs = now
                                    }
                                }
                            }
                        }
                    }
            ) {
                TextH3(
                    text = "GranaWiz",
                    modifier = Modifier.align(Alignment.Center),
                    align = TextAlign.Center
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TitleBarButton(
                        icon = PhosphorIcons.Light.Minus,
                        onClick = { window.isMinimized = true }
                    )

                    val isMaximized = windowState.placement == WindowPlacement.Maximized
                    TitleBarButton(
                        icon = if (isMaximized) PhosphorIcons.Light.ArrowsIn else PhosphorIcons.Light.ArrowsOut,
                        onClick = {
                            windowState.placement =
                                if (isMaximized) WindowPlacement.Floating else WindowPlacement.Maximized
                        }
                    )

                    TitleBarButton(
                        icon = PhosphorIcons.Light.X,
                        //hoverBackground = Color(0xFFEF5350).copy(alpha = 0.15f),
                        //hoverTint = Color(0xFFEF5350),
                        onClick = onCloseRequest
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(MaterialTheme.colors.onSurface)
        )
    }
}

@Composable
private fun TitleBarButton(
    icon: ImageVector,
    hoverBackground: Color = MaterialTheme.colors.onSurface,
    hoverTint: Color = MaterialTheme.colors.primary,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(if (isHovered) hoverBackground else Color.Transparent)
            .hoverable(interactionSource)
            .clickable(onClick = onClick)
            .pointerHoverIcon(PointerIcon.Hand),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = if (isHovered) hoverTint else MaterialTheme.colors.secondary
        )
    }
}
