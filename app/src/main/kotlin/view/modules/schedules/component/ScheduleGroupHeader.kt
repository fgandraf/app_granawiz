package view.modules.schedules.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import view.shared.TextH1

@Composable
fun ScheduleGroupHeader(
    modifier: Modifier = Modifier,
    title: String,
) {
    Box(modifier.padding(start = 80.dp)) {
        TextH1(text = title)
    }
}
