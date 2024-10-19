package ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import ui.theme.SidebarColors

@Composable
fun Sidebar() {
    val primaryVariant = MaterialTheme.colors.primaryVariant

    Column(
        modifier = Modifier
            .width(280.dp)
            .fillMaxHeight()
            .background(
                Brush.linearGradient(
                    start = Offset(0f, Float.POSITIVE_INFINITY),
                    end = Offset(280f, 0f),
                    colors = SidebarColors
                )
            )
            .drawBehind {
                drawLine(
                    color = primaryVariant,
                    start = Offset(279.5.dp.toPx(), 0f),
                    end = Offset(279.5.dp.toPx(), size.height),
                    strokeWidth = 0.5.dp.toPx()
                )
            }
    ) {



        Row(modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .drawBehind {
                drawLine(
                    color = primaryVariant,
                    start = Offset(0f, 69.5.dp.toPx()),
                    end = Offset(280.dp.toPx(), 69.5.dp.toPx()),
                    strokeWidth = 0.5.dp.toPx()
                )
            }
        ) { }



        Row(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
        )
        { }



        Row(modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .drawBehind {
                drawLine(
                    color = primaryVariant,
                    start = Offset(0f, 0.5.dp.toPx()),
                    end = Offset(280.dp.toPx(), 0.5.dp.toPx()),
                    strokeWidth = 0.5.dp.toPx()
                )
                }
        )
        { }



    }
}
