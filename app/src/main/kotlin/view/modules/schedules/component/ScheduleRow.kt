package view.modules.schedules.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Light
import com.adamglin.phosphoricons.light.Check
import com.adamglin.phosphoricons.light.DotsThree
import com.adamglin.phosphoricons.light.Pen
import com.adamglin.phosphoricons.light.Repeat
import com.adamglin.phosphoricons.light.Tag
import com.adamglin.phosphoricons.light.Trash
import core.enums.ScheduleFrequency
import core.enums.TransactionType
import domain.schedule.usecases.ScheduleOccurrence
import utils.IconPaths
import utils.brMoney
import utils.rememberSvgPainter
import view.shared.*
import viewModel.ScheduleViewModel
import java.time.format.TextStyle
import java.util.*

@Composable
fun ScheduleRow(
    viewModel: ScheduleViewModel,
    occurrence: ScheduleOccurrence,
    overdue: Boolean,
    onEdit: () -> Unit,
) {
    val schedule = occurrence.schedule
    val dueDate = occurrence.dueDate

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .clip(RoundedCornerShape(0.dp))
            .pointerHoverIcon(PointerIcon.Hand)
            .clickable { onEdit() }
    ) {

        // Type dot
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxHeight().padding(start = 30.dp, end = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(
                        if (schedule.type == TransactionType.GAIN) MaterialTheme.colors.onPrimary
                        else if (schedule.type == TransactionType.EXPENSE) MaterialTheme.colors.onError
                        else MaterialTheme.colors.primaryVariant
                    )
                    .size(10.dp)
            )
        }

        // Party + due date
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight().weight(0.9f).padding(end = 10.dp)
        ) {
            TextNormal(
                modifier = Modifier.padding(bottom = 2.dp),
                text = schedule.party.name
            )
            val day = dueDate.dayOfMonth
            val month = dueDate.month.getDisplayName(TextStyle.FULL, Locale.of("pt", "BR"))
            val color = if (overdue) MaterialTheme.colors.onError else MaterialTheme.colors.primary
            TextSmall(text = "Vence em $day $month", color = color)
        }

        // Category + subcategory
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxHeight().weight(0.9f).padding(end = 10.dp)
        ) {
            Icon(
                painter = rememberSvgPainter(IconPaths.CATEGORY_PACK + schedule.category.icon),
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(15.dp)
            )
            Spacer(Modifier.width(10.dp))
            TextNormal(text = schedule.category.name)
            if (schedule.subcategory != null) {
                Spacer(Modifier.width(5.dp))
                TextNormal(text = "→")
                Spacer(Modifier.width(5.dp))
                TextNormal(text = schedule.subcategory!!.name)
            }
        }

        // Frequency badge + tags
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxHeight().weight(0.9f).padding(end = 10.dp)
        ) {
            Icon(
                imageVector = PhosphorIcons.Light.Repeat,
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(15.dp)
            )
            Spacer(Modifier.width(5.dp))
            TextSmall(text = frequencyLabel(schedule.frequency, schedule.interval))
            if (!schedule.tags.isNullOrEmpty()) {
                Spacer(Modifier.width(15.dp))
                schedule.tags?.forEach { tag ->
                    Row {
                        Icon(
                            imageVector = PhosphorIcons.Light.Tag,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(Modifier.width(5.dp))
                        TextNormal(text = tag.name)
                        Spacer(Modifier.width(15.dp))
                    }
                }
            }
        }

        // Balance
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxHeight().padding(end = 10.dp).weight(0.4f)
        ) {
            TextNormal(text = brMoney.format(schedule.balance))
        }

        // Mark as paid
        Row(
            Modifier.fillMaxHeight().padding(end = 8.dp),
            Arrangement.End,
            Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .border(1.dp, MaterialTheme.colors.onPrimary, CircleShape)
                    .pointerHoverIcon(PointerIcon.Hand)
                    .clickable { viewModel.markAsPaid(occurrence) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = PhosphorIcons.Light.Check,
                    contentDescription = "Marcar como pago",
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // More menu
        var showMenu by remember { mutableStateOf(false) }
        Row(Modifier.fillMaxHeight().padding(end = 20.dp), Arrangement.End, Alignment.CenterVertically) {
            ClickableIcon(
                icon = PhosphorIcons.Light.DotsThree,
                shape = RoundedCornerShape(6.dp),
                onClick = { showMenu = true },
            )

            if (showMenu) {
                DropdownMenu(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    var triggerEdit by remember { mutableStateOf(false) }
                    ClickableRow(icon = PhosphorIcons.Light.Pen, label = "Editar") {
                        triggerEdit = true
                    }
                    if (triggerEdit) {
                        onEdit()
                        showMenu = false
                    }

                    var confirmDelete by remember { mutableStateOf(false) }
                    ClickableRow(icon = PhosphorIcons.Light.Trash, label = "Excluir") {
                        confirmDelete = true
                    }
                    if (confirmDelete) {
                        SimpleQuestionDialog(
                            message = "Tem certeza que deseja excluir esse agendamento? Todas as ocorrências futuras serão removidas.",
                            onConfirmRequest = {
                                viewModel.deleteSchedule(schedule)
                                confirmDelete = false
                                showMenu = false
                            },
                            onDismissRequest = { confirmDelete = false; showMenu = false },
                        )
                    }
                }
            }
        }
    }
}

private fun frequencyLabel(frequency: ScheduleFrequency, interval: Int): String {
    val base = when (frequency) {
        ScheduleFrequency.ONCE -> "Uma única vez"
        ScheduleFrequency.DAILY -> if (interval == 1) "Diário" else "A cada $interval dias"
        ScheduleFrequency.WEEKLY -> if (interval == 1) "Semanal" else "A cada $interval semanas"
        ScheduleFrequency.MONTHLY -> if (interval == 1) "Mensal" else "A cada $interval meses"
        ScheduleFrequency.YEARLY -> if (interval == 1) "Anual" else "A cada $interval anos"
    }
    return base
}
