package view.modules.importStatement.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import view.shared.TextSmall
import view.theme.ButtonPurple

@Composable
fun WizardStepIndicator(currentStep: Int) {
    val steps = listOf(
        stringResource(Res.string.import_step_file),
        stringResource(Res.string.import_step_analysis),
        stringResource(Res.string.import_step_conciliation),
        stringResource(Res.string.import_step_import_label),
    )
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        steps.forEachIndexed { index, label ->
            val isActive = index == currentStep
            val isDone = index < currentStep

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(
                            if (isActive || isDone) ButtonPurple
                            else MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
                        )
                ) {
                    TextSmall(
                        text = "${index + 1}",
                        color = if (isActive || isDone) MaterialTheme.colors.surface
                        else MaterialTheme.colors.primary.copy(alpha = 0.5f)
                    )
                }
                Spacer(Modifier.height(4.dp))
                TextSmall(
                    text = label,
                    color = if (isActive) MaterialTheme.colors.primary
                    else MaterialTheme.colors.primary.copy(alpha = 0.5f)
                )
            }

            if (index < steps.lastIndex) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .padding(bottom = 20.dp)
                        .height(1.dp)
                        .width(60.dp)
                        .background(
                            if (index < currentStep) ButtonPurple
                            else MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
                        )
                )
            }
        }
    }
}
