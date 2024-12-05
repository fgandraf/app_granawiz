package view.modules.transactionForm.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import view.shared.DefaultButton
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*


@Composable
fun DateTimePicker(
    modifier: Modifier = Modifier,
    value: LocalDateTime,
    label: String,
    trailingIcon: ImageVector? = Icons.Default.CalendarMonth,
    primaryColor: Color = Color.Black,
    selectedColor: Color = Color.Blue,
    fontFamily: FontFamily = FontFamily.Default,
    datePattern: String = "MM/dd/yyyy HH:mm",
    selectedDateTime: (LocalDateTime) -> Unit,
){
    val dialogPicker = remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 10.sp,
            lineHeight = 0.sp,
            color = primaryColor,
            fontFamily = fontFamily,
            modifier = Modifier.padding(bottom = 5.dp)
        )

        val secondaryColor = MaterialTheme.colors.secondary
        var borderSize by remember { mutableStateOf(1.dp) }
        var borderColor by remember { mutableStateOf(primaryColor) }

        Box(contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp)
                .border(borderSize, borderColor, shape = RoundedCornerShape(5.dp))
                .clip(RoundedCornerShape(5.dp))
                .pointerHoverIcon(PointerIcon.Hand)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused){ borderSize = 1.2.dp; borderColor = secondaryColor }
                    else { borderSize = 1.dp; borderColor = primaryColor }
                }
                .clickable { dialogPicker.value = true }
                .padding(horizontal = 10.dp)
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = value.format(DateTimeFormatter.ofPattern(datePattern)),
                    fontSize = 12.sp,
                    lineHeight = 0.sp,
                    color = primaryColor,
                    fontFamily = fontFamily
                )
                if (trailingIcon != null) {
                    Icon(imageVector = trailingIcon, contentDescription = "Icon")
                }
            }
        }

        if (dialogPicker.value){
            DateTimeDialog(
                expanded = dialogPicker.value,
                primaryColor = primaryColor,
                selectedColor = selectedColor,
                fontFamily = fontFamily,
                selectedDateTime = value,
                onDateTimeSelected = {selectedDateTime(it)},
                onDismissRequest = {dialogPicker.value = false}
            )
        }
    }
}

@Composable
fun DateTimeDialog(
    expanded: Boolean,
    showTime: Boolean = true,
    primaryColor: Color = Color.Gray,
    selectedColor: Color = Color.Blue,
    language: String = "pt",
    country: String = "br",
    weekDayNames: List<String> = listOf("Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sab"),
    fontFamily: FontFamily = FontFamily.Default,
    onDismissRequest: () -> Unit,
    selectedDateTime: LocalDateTime,
    onDateTimeSelected: (LocalDateTime) -> Unit
) {
    if (expanded) {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                modifier = Modifier.width(300.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = 8.dp
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 10.dp).padding(top = 10.dp, bottom = 5.dp)) {

                    // DATE PICKER
                    var selectedDate by remember { mutableStateOf(selectedDateTime.toLocalDate()) }
                    DatePicker(
                        selectedDate = selectedDate,
                        primaryColor = primaryColor,
                        selectedColor = selectedColor,
                        language = language,
                        country = country,
                        weekDayNames = weekDayNames,
                        fontFamily = fontFamily,
                        onDateSelected = { selectedDate = it }
                    )

                    // TIME PICKER
                    var selectedTime by remember { mutableStateOf(selectedDateTime.toLocalTime()) }
                    if (showTime){
                        TimePicker(
                            selectedTime = selectedTime,
                            primaryColor = primaryColor,
                            onTimeSelected = { selectedTime = it },
                        )
                    }

                    // FOOTER
                    DefaultButton(
                        label = "Confirmar",
                        onClick = {
                            onDateTimeSelected(LocalDateTime.of(selectedDate, selectedTime))
                            onDismissRequest()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DatePicker(
    selectedDate: LocalDate,
    primaryColor: Color,
    selectedColor: Color,
    language: String,
    country: String,
    weekDayNames: List<String>,
    fontFamily: FontFamily,
    onDateSelected: (LocalDate) -> Unit
) {
    var currentMonth by remember { mutableStateOf(YearMonth.from(selectedDate)) }
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = currentMonth.atDay(1).dayOfWeek.value % 7 // Ajuste para começar no domingo
    val weeks = remember(currentMonth) {
        mutableListOf<List<LocalDate?>>().apply {
            var week = mutableListOf<LocalDate?>()
            for (i in 0 until firstDayOfWeek) {
                week.add(null)
            }
            for (day in 1..daysInMonth) {
                week.add(currentMonth.atDay(day))
                if (week.size == 7) {
                    add(week)
                    week = mutableListOf()
                }
            }
            if (week.isNotEmpty()) {
                while (week.size < 7) {
                    week.add(null)
                }
                add(week)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp)
    ) {
        // MONTH SELECTOR
        val month = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.of(language, country)).replaceFirstChar {it.uppercase()}
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$month ${ currentMonth.year}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = primaryColor,
                fontFamily = fontFamily,
                lineHeight = 0.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand).clip(CircleShape).size(30.dp).padding(8.dp),
                    onClick = { currentMonth = currentMonth.minusMonths(1) }
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month", tint = primaryColor)
                }
                IconButton(
                    modifier = Modifier.pointerHoverIcon(PointerIcon.Hand).clip(CircleShape).size(30.dp).padding(8.dp),
                    onClick = { currentMonth = currentMonth.plusMonths(1) }
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month", tint = primaryColor)
                }
            }
        }
        Divider()
        // WEEKDAY HEADER
        Row(modifier = Modifier.fillMaxWidth()) {
            for (dayName in weekDayNames) {
                Text(
                    text = dayName,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    color = primaryColor,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        // DAYS
        for (week in weeks) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (date in week) {
                    if (date != null) {
                        val isSelected = date == selectedDate
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .pointerHoverIcon(PointerIcon.Hand)
                                .background(
                                    color = if (isSelected) selectedColor else Color.Transparent,
                                    shape = MaterialTheme.shapes.small
                                )
                                .clickable { onDateSelected(date) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = date.dayOfMonth.toString(),
                                fontSize = 12.sp,
                                lineHeight = 0.sp,
                                color = if (isSelected) Color.White else primaryColor,
                                fontFamily = fontFamily
                            )
                        }
                    } else {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        )
                    }
                }
            }
        }
        Divider(Modifier.padding(top = 10.dp))
    }
}

@Composable
fun TimePicker(
    selectedTime: LocalTime,
    primaryColor: Color,
    onTimeSelected: (LocalTime) -> Unit
){
    var hours by remember { mutableStateOf(selectedTime.hour) }
    var minutes by remember { mutableStateOf(selectedTime.minute) }
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hour selector UP
            NumberPicker(
                value = hours,
                range = 0..23,
                primaryColor = primaryColor,
                onValueChange = { hours = it },
            )
            Text(":", fontSize = 14.sp, modifier = Modifier.padding(horizontal = 5.dp))
            // Minute selector
            NumberPicker(
                value = minutes,
                range = 0..59,
                primaryColor = primaryColor,
                onValueChange = { minutes = it },
            )
        }

        Divider(Modifier.padding(top = 10.dp))
    }
    onTimeSelected(LocalTime.of(hours, minutes))
}

@Composable
fun NumberPicker(
    value: Int,
    range: IntRange,
    primaryColor: Color,
    onValueChange: (Int) -> Unit
) {
    Column {
        // Selector UP
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(50.dp).height(20.dp)
                .clip(RoundedCornerShape(6.dp))
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable {
                    val newValue = if (value == range.last) range.first
                    else value + 1
                    onValueChange(newValue)
                }
        ) {
            Icon(Icons.Filled.ExpandLess, contentDescription = "Up", tint = primaryColor)
        }
        // Value Text
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(50.dp)) {
            Text(
                text = value.toString().padStart(2, '0'),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
        // Selector Down
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(50.dp).height(20.dp)
                .clip(RoundedCornerShape(6.dp))
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable {
                    val newValue = if (value == range.first) range.last
                    else value - 1
                    onValueChange(newValue)
                }
        ) {
            Icon(Icons.Filled.ExpandMore, contentDescription = "Down", tint = primaryColor)
        }
    }
}
