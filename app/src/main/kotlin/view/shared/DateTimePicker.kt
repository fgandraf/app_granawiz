package view.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import view.theme.DefaultFont
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.light.Calendar
import com.adamglin.phosphoricons.regular.ArrowDown
import com.adamglin.phosphoricons.regular.ArrowLeft
import com.adamglin.phosphoricons.regular.ArrowRight
import com.adamglin.phosphoricons.regular.ArrowUp
import com.felipegandra.generated.resources.Res
import com.felipegandra.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import utils.generateWeeks
import view.theme.ButtonPurple
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DateTimePicker(
    modifier: Modifier = Modifier,
    showLabel: Boolean = true,
    value: LocalDateTime,
    showBorder: Boolean = true,
    borderOnActive: Boolean = false,
    selectedDateTime: (LocalDateTime) -> Unit,
) {

    Column(modifier = modifier) {
        var expanded by remember { mutableStateOf(false) }
        val formatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm") }

        var selectedDate by remember { mutableStateOf(value.toLocalDate()) }
        var currentMonth by remember { mutableStateOf(YearMonth.from(selectedDate)) }
        var hours by remember { mutableStateOf(value.hour) }
        var minutes by remember { mutableStateOf(value.minute) }
        var textInput by remember { mutableStateOf(value.format(formatter)) }

        val inputTextStyle = androidx.compose.ui.text.TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = DefaultFont,
            color = MaterialTheme.colors.primary,
        )

        val effectiveShowBorder = if (borderOnActive) expanded else showBorder

        if (showLabel) TextSmall(modifier = Modifier.padding(bottom = 5.dp), text = stringResource(Res.string.form_field_date_time))

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {

            FocusableBox(showBorder = effectiveShowBorder) {
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    BasicTextField(
                        value = textInput,
                        onValueChange = { newText ->
                            textInput = newText
                            runCatching { LocalDateTime.parse(newText, formatter) }
                                .onSuccess { parsed ->
                                    selectedDate = parsed.toLocalDate()
                                    currentMonth = YearMonth.from(parsed.toLocalDate())
                                    hours = parsed.hour
                                    minutes = parsed.minute
                                    selectedDateTime(parsed)
                                }
                        },
                        singleLine = true,
                        textStyle = inputTextStyle,
                        cursorBrush = SolidColor(MaterialTheme.colors.primary),
                        modifier = Modifier.weight(1f),
                    )
                    Icon(
                        imageVector = PhosphorIcons.Light.Calendar,
                        contentDescription = "Icon",
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.widthIn(min = 260.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 10.dp).padding(top = 10.dp, bottom = 5.dp)
                ) {
                    // DATE PICKER
                    val weeks = remember(currentMonth) { generateWeeks(currentMonth) }

                    Column {
                        //--month selector
                        val month = currentMonth.month.getDisplayName(TextStyle.FULL, Locale.of("pt", "br"))
                            .replaceFirstChar { it.uppercase() }
                        Row(
                            Modifier.fillMaxWidth().padding(bottom = 5.dp),
                            Arrangement.SpaceBetween,
                            Alignment.CenterVertically
                        ) {
                            TextNormal(text = "$month ${currentMonth.year}", align = TextAlign.Center)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                ClickableIcon(
                                    icon = PhosphorIcons.Regular.ArrowLeft,
                                    iconSize = 15.dp, boxSize = 25.dp,
                                    shape = CircleShape,
                                    onClick = { currentMonth = currentMonth.minusMonths(1) }
                                )
                                ClickableIcon(
                                    icon = PhosphorIcons.Regular.ArrowRight,
                                    iconSize = 15.dp, boxSize = 25.dp,
                                    shape = CircleShape,
                                    onClick = { currentMonth = currentMonth.plusMonths(1) }
                                )
                            }
                        }
                        Divider()
                        //--weekday header
                        val weekDayNames = listOf(DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)
                            .map { it.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.of("pt", "br")).take(3).replaceFirstChar { c -> c.uppercase() } }
                        Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
                            for (dayName in weekDayNames) {
                                TextSmall(
                                    modifier = Modifier.weight(1f),
                                    text = dayName,
                                    align = TextAlign.Center
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        //--days
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
                                                    color = if (isSelected) ButtonPurple else Color.Transparent,
                                                    shape = MaterialTheme.shapes.small
                                                )
                                                .clickable {
                                                    selectedDate = date
                                                    val newDateTime = LocalDateTime.of(date, LocalTime.of(hours, minutes))
                                                    textInput = newDateTime.format(formatter)
                                                    selectedDateTime(newDateTime)
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            TextNormal(
                                                text = date.dayOfMonth.toString(),
                                                color = if (isSelected) Color.White else MaterialTheme.colors.primary
                                            )
                                        }
                                    } else
                                        Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
                                }
                            }
                        }
                        Divider(Modifier.padding(top = 10.dp))
                    }

                    // TIME PICKER
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            NumberPicker(
                                value = hours,
                                range = 0..23,
                                onValueChange = { newHours ->
                                    hours = newHours
                                    val newDateTime = LocalDateTime.of(selectedDate, LocalTime.of(newHours, minutes))
                                    textInput = newDateTime.format(formatter)
                                    selectedDateTime(newDateTime)
                                },
                            )
                            TextNormal(modifier = Modifier.padding(horizontal = 5.dp), text = ":")
                            NumberPicker(
                                value = minutes,
                                range = 0..59,
                                onValueChange = { newMinutes ->
                                    minutes = newMinutes
                                    val newDateTime = LocalDateTime.of(selectedDate, LocalTime.of(hours, newMinutes))
                                    textInput = newDateTime.format(formatter)
                                    selectedDateTime(newDateTime)
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NumberPicker(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit,
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
            Icon(PhosphorIcons.Regular.ArrowUp, contentDescription = "Up", tint = MaterialTheme.colors.primary)
        }
        // Value Text
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(50.dp)) {
            TextNormal(text = value.toString().padStart(2, '0'), align = TextAlign.Center)
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
            Icon(PhosphorIcons.Regular.ArrowDown, contentDescription = "Down", tint = MaterialTheme.colors.primary)
        }
    }
}