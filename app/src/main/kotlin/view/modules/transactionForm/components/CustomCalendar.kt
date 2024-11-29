package view.modules.transactionForm.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import view.shared.ClickableIcon
import view.shared.TextPrimary
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CustomCalendar(
    selectedDate: LocalDate,
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
        modifier = Modifier.fillMaxWidth(0.5f)
    ) {


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ClickableIcon(
                modifier = Modifier,
                icon = "arrow-left",
                shape = CircleShape,
                iconSize = 20.dp,
                padding = true,
                onClick = { currentMonth = currentMonth.minusMonths(1) }
            )

            val month = currentMonth.month.name
            TextPrimary(text = month + " " + currentMonth.year, weight = FontWeight.Bold)

            ClickableIcon(
                modifier = Modifier,
                icon = "arrow-right",
                shape = CircleShape,
                iconSize = 20.dp,
                padding = true,
                onClick = { currentMonth = currentMonth.plusMonths(1) }
            )

        }

        Divider(Modifier.padding(top = 2.dp, bottom = 12.dp))

        // Cabeçalho dos dias da semana com as primeiras letras
        val dayNames = listOf("D", "S", "T", "Q", "Q", "S", "S")
        Row(modifier = Modifier.fillMaxWidth()) {
            for (dayName in dayNames) {
                TextPrimary(
                    text = dayName,
                    align = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Dias do mês
        for (week in weeks) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (date in week) {
                    if (date != null) {
                        val isSelected = date == selectedDate
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(4.dp)
                                .background(
                                    if (isSelected) MaterialTheme.colors.primary else Color.Transparent,
                                    shape = MaterialTheme.shapes.small
                                )
                                .clickable {
                                    onDateSelected(date)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            TextPrimary(
                                text = date.dayOfMonth.toString(),
                                color = if (isSelected) Color.White else Color.Unspecified
                            )
                        }
                    } else {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
        Divider()
    }
}
