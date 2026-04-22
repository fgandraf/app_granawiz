package view.modules.schedules.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.CaretDown
import core.enums.ScheduleFrequency
import view.shared.DateTimePicker
import view.shared.FocusableBox
import view.shared.TextNormal
import view.shared.TextSmall
import view.theme.ButtonPurple
import viewModel.ScheduleFormViewModel

enum class TerminationMode { NEVER, INSTALLMENTS, END_DATE }

@Composable
fun RecurrenceSection(viewModel: ScheduleFormViewModel) {

    val frequency = viewModel.frequency
    val intervalLabel = when (frequency) {
        ScheduleFrequency.ONCE -> ""
        ScheduleFrequency.DAILY -> if (viewModel.interval == 1) "dia" else "dias"
        ScheduleFrequency.WEEKLY -> if (viewModel.interval == 1) "semana" else "semanas"
        ScheduleFrequency.MONTHLY -> if (viewModel.interval == 1) "mês" else "meses"
        ScheduleFrequency.YEARLY -> if (viewModel.interval == 1) "ano" else "anos"
    }

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {

        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
            FrequencyDropdown(
                modifier = if (frequency == ScheduleFrequency.ONCE) Modifier.fillMaxWidth() else Modifier.weight(1f),
                value = frequency,
                onChange = {
                    viewModel.frequency = it
                    if (it == ScheduleFrequency.ONCE) {
                        viewModel.interval = 1
                        viewModel.installments = null
                        viewModel.endDate = null
                        viewModel.dayOfMonth = null
                    }
                }
            )

            if (frequency != ScheduleFrequency.ONCE) {
                Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                    TextSmall(text = "A cada:", modifier = Modifier.padding(bottom = 5.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IntStepper(
                            value = viewModel.interval,
                            min = 1,
                            max = 365,
                            onChange = { viewModel.interval = it }
                        )
                        TextNormal(
                            text = intervalLabel,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }
            }
        }

        if (frequency == ScheduleFrequency.MONTHLY) {
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                TextSmall(text = "Dia do mês:", modifier = Modifier.padding(bottom = 5.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val current = viewModel.dayOfMonth ?: viewModel.startDate.dayOfMonth
                    IntStepper(
                        value = current,
                        min = 1,
                        max = 31,
                        onChange = { viewModel.dayOfMonth = it }
                    )
                    TextSmall(
                        modifier = Modifier.padding(start = 10.dp),
                        text = "(se o mês tiver menos dias, cai no último dia)"
                    )
                }
            }
        }

        if (frequency == ScheduleFrequency.ONCE) return@Column

        var mode by remember {
            mutableStateOf(
                when {
                    viewModel.installments != null -> TerminationMode.INSTALLMENTS
                    viewModel.endDate != null -> TerminationMode.END_DATE
                    else -> TerminationMode.NEVER
                }
            )
        }

        TextSmall(text = "Termina em:", modifier = Modifier.padding(bottom = 5.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TerminationRadio(
                label = "Nunca",
                selected = mode == TerminationMode.NEVER,
                onClick = {
                    mode = TerminationMode.NEVER
                    viewModel.installments = null
                    viewModel.endDate = null
                }
            )
            Spacer(Modifier.width(15.dp))
            TerminationRadio(
                label = "Após",
                selected = mode == TerminationMode.INSTALLMENTS,
                onClick = {
                    mode = TerminationMode.INSTALLMENTS
                    if (viewModel.installments == null) viewModel.installments = 12
                    viewModel.endDate = null
                }
            )
            if (mode == TerminationMode.INSTALLMENTS) {
                Spacer(Modifier.width(8.dp))
                IntStepper(
                    value = viewModel.installments ?: 12,
                    min = 1,
                    max = 999,
                    onChange = { viewModel.installments = it }
                )
                TextNormal(
                    text = "parcela(s)",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Spacer(Modifier.width(15.dp))
            TerminationRadio(
                label = "Em data",
                selected = mode == TerminationMode.END_DATE,
                onClick = {
                    mode = TerminationMode.END_DATE
                    if (viewModel.endDate == null) viewModel.endDate = viewModel.startDate.plusYears(1)
                    viewModel.installments = null
                }
            )
        }
        if (mode == TerminationMode.END_DATE) {
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                DateTimePicker(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    value = viewModel.endDate ?: viewModel.startDate.plusYears(1),
                    selectedDateTime = { viewModel.endDate = it }
                )
            }
        }
    }
}

@Composable
private fun FrequencyDropdown(
    modifier: Modifier = Modifier,
    value: ScheduleFrequency,
    onChange: (ScheduleFrequency) -> Unit,
) {
    val label = when (value) {
        ScheduleFrequency.ONCE -> "Uma única vez"
        ScheduleFrequency.DAILY -> "Diária"
        ScheduleFrequency.WEEKLY -> "Semanal"
        ScheduleFrequency.MONTHLY -> "Mensal"
        ScheduleFrequency.YEARLY -> "Anual"
    }
    Column(modifier = modifier) {
        TextSmall(text = "Frequência:", modifier = Modifier.padding(bottom = 5.dp))
        var expanded by remember { mutableStateOf(false) }
        Box {
            FocusableBox(onClick = { expanded = true }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextNormal(text = label)
                    Icon(
                        imageVector = PhosphorIcons.Light.CaretDown,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                ScheduleFrequency.entries.forEach { freq ->
                    val freqLabel = when (freq) {
                        ScheduleFrequency.ONCE -> "Uma única vez"
                        ScheduleFrequency.DAILY -> "Diária"
                        ScheduleFrequency.WEEKLY -> "Semanal"
                        ScheduleFrequency.MONTHLY -> "Mensal"
                        ScheduleFrequency.YEARLY -> "Anual"
                    }
                    DropdownMenuItem(onClick = {
                        onChange(freq)
                        expanded = false
                    }) {
                        TextNormal(text = freqLabel)
                    }
                }
            }
        }
    }
}

@Composable
private fun IntStepper(
    value: Int,
    min: Int,
    max: Int,
    onChange: (Int) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        StepperButton(label = "−", onClick = { if (value > min) onChange(value - 1) })
        Box(
            modifier = Modifier
                .width(45.dp).height(28.dp)
                .border(1.dp, MaterialTheme.colors.primary, RoundedCornerShape(5.dp)),
            contentAlignment = Alignment.Center
        ) {
            TextNormal(text = value.toString(), align = TextAlign.Center)
        }
        StepperButton(label = "+", onClick = { if (value < max) onChange(value + 1) })
    }
}

@Composable
private fun StepperButton(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(RoundedCornerShape(5.dp))
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        TextNormal(text = label)
    }
}

@Composable
private fun TerminationRadio(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand).clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(if (selected) ButtonPurple else Color.Transparent)
                .border(1.dp, MaterialTheme.colors.primary, CircleShape)
        )
        TextNormal(text = label, modifier = Modifier.padding(start = 6.dp))
    }
}
