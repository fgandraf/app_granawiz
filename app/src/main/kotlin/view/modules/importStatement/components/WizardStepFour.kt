package view.modules.importStatement.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import application.importStatement.LogLevel
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import view.shared.DefaultButton
import view.shared.TextH1
import view.shared.TextNormal
import viewModel.ImportStatementViewModel
import viewModel.WizardLogLine

private val WarningOrange = Color(0xFFE67E22)

@Composable
fun WizardStepFour(
    viewModel: ImportStatementViewModel,
    onFinish: () -> Unit,
) {
    val logLines by viewModel.importLog.collectAsState()
    val isImporting by viewModel.isImporting.collectAsState()
    val importDone by viewModel.importDone.collectAsState()
    val importFailed by viewModel.importFailed.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.executeImport()
    }

    val listState = rememberLazyListState()
    LaunchedEffect(logLines.size) {
        if (logLines.isNotEmpty()) listState.animateScrollToItem(logLines.size - 1)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        TextH1(text = stringResource(Res.string.import_step4_title))
        Spacer(Modifier.height(8.dp))
        TextNormal(text = stringResource(Res.string.import_step4_instruction))

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(0.5.dp, MaterialTheme.colors.onSurface, RoundedCornerShape(6.dp))
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colors.background)
                .padding(12.dp)
        ) {
            LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                items(logLines) { line ->
                    TextNormal(
                        text = line.text,
                        color = logLevelColor(line),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        fontSize = 11.sp
                    )
                }
            }
            VerticalScrollbar(
                adapter = rememberScrollbarAdapter(listState),
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        Spacer(Modifier.height(16.dp))

        Divider(modifier = Modifier.padding(bottom = 16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            DefaultButton(
                modifier = Modifier.width(200.dp),
                text = if (importFailed && !importDone) stringResource(Res.string.close) else stringResource(Res.string.finish),
                confirmed = !isImporting,
                textColor = if (MaterialTheme.colors.isLight) Color.White else MaterialTheme.colors.secondary,
                onClick = onFinish
            )
        }
    }
}

@Composable
private fun logLevelColor(line: WizardLogLine): Color = when (line.level) {
    LogLevel.OK -> MaterialTheme.colors.onPrimary
    LogLevel.WARN -> WarningOrange
    LogLevel.ERROR -> MaterialTheme.colors.onError
    LogLevel.INFO -> MaterialTheme.colors.primary.copy(alpha = 0.8f)
}
