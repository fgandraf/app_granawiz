package view.shared

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import config.IconPaths
import view.modules.transactionForm.components.FriendlyDateTimePicker
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
fun DateTimePicker(
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
            FriendlyDateTimePicker(
                expanded = dialogPicker.value,
                selectedDateTime = value,
                onDateTimeSelected = {selectedDateTime(it)},
                onDismissRequest = {dialogPicker.value = false}
            )

        }

    }
}