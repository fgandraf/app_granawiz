package view.shared

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import config.IconPaths
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun DateTimeDialog(
    modifier: Modifier = Modifier,
    value: LocalDateTime,
    label: String,
    selectedDateTime: (LocalDateTime) -> Unit,
){

    val dialogPicker = remember { mutableStateOf(false) }

    Column(modifier = modifier) {

        TextPrimary(text = label, modifier = Modifier.padding(bottom = 5.dp), size = 10.sp)

        FocusableBox(onClick = { dialogPicker.value = true }) {

            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ){
                TextPrimary(text = value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                Icon(painter = painterResource(IconPaths.SYSTEM_ICONS + "schedule.svg"), contentDescription = "Calendar Icon")
            }

        }


        if (dialogPicker.value){
            DateTimeDialog(
                expanded = dialogPicker,
                initialDateTime = value,
                onDismissRequest = { dialogPicker.value = false },
                onDateTimeSelected = { itDate, itTime -> selectedDateTime(LocalDateTime.of(itDate, itTime)) }
            )
        }

    }
}



@Composable
fun DateTimeDialog(
    expanded: MutableState<Boolean>,
    initialDateTime: LocalDateTime,
    onDismissRequest: () -> Unit,
    onDateTimeSelected: (LocalDate, LocalTime) -> Unit
){
    var selectedDate by remember { mutableStateOf(initialDateTime.toLocalDate()) }
    var selectedTime by remember { mutableStateOf(initialDateTime.toLocalTime()) }


    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = onDismissRequest
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Date Picker
            Text("Select Date")
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { selectedDate = selectedDate.minusDays(1) }) {
                    Text("-")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { selectedDate = selectedDate.plusDays(1) }) {
                    Text("+")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Time Picker
            Text("Select Time")
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { selectedTime = selectedTime.minusMinutes(1) }) {
                    Text("-")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(selectedTime.format(DateTimeFormatter.ofPattern("HH:mm")))
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { selectedTime = selectedTime.plusMinutes(1) }) {
                    Text("+")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Button
            Button(onClick = {
                onDateTimeSelected(selectedDate, selectedTime)
                expanded.value = false
            }) {
                Text("Confirm")
            }
        }
    }
}