package view.modules.transactionForm.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun FriendlyDateTimePicker(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    selectedDateTime: LocalDateTime,
    onDateTimeSelected: (LocalDateTime) -> Unit
) {

    if (expanded) {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                modifier = Modifier.wrapContentSize(),
                shape = RoundedCornerShape(16.dp),
                elevation = 8.dp
            ) {
                Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                    // Calendário Personalizado
                    var selectedDate by remember { mutableStateOf(selectedDateTime.toLocalDate()) }
                    CustomCalendar(
                        selectedDate = selectedDate,
                        onDateSelected = { selectedDate = it }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Seletor de Hora
                    Text("Selecione a Hora", style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.height(8.dp))
                    var selectedTime by remember { mutableStateOf(selectedDateTime.toLocalTime()) }

                    TimePicker(
                        selectedTime = selectedTime,
                        onTimeSelected = {
                            selectedTime = it
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            onDateTimeSelected(LocalDateTime.of(selectedDate, selectedTime))
                            onDismissRequest()
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Confirmar")
                    }
                }
            }
        }
    }
}

@Composable
fun TimePicker(
    selectedTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit
) {
    var hours by remember { mutableStateOf(selectedTime.hour) }
    var minutes by remember { mutableStateOf(selectedTime.minute) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        // Selector de Horas
        NumberPicker(
            value = hours,
            range = 0..23,
            onValueChange = { hours = it }
        )
        Text(":", fontSize = 14.sp, modifier = Modifier.padding(horizontal = 8.dp))
        // Selector de Minutos
        NumberPicker(
            value = minutes,
            range = 0..59,
            onValueChange = { minutes = it }
        )
    }

    onTimeSelected(LocalTime.of(hours, minutes))
}

@Composable
fun NumberPicker(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = {
            val newValue = if (value > range.first) value - 1 else range.last
            onValueChange(newValue)
        }) {
            Icon(Icons.Default.Refresh, contentDescription = "Diminuir")
        }
        Text(
            text = value.toString().padStart(2, '0'),
            fontSize = 14.sp,
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.Center
        )
        IconButton(onClick = {
            val newValue = if (value < range.last) value + 1 else range.first
            onValueChange(newValue)
        }) {
            Icon(Icons.Default.Add, contentDescription = "Aumentar")
        }
    }
}
