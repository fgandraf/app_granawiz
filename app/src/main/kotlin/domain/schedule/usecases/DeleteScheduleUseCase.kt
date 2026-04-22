package domain.schedule.usecases

import core.entity.Schedule
import infra.dao.ScheduleDao

class DeleteScheduleUseCase(private val scheduleDao: ScheduleDao = ScheduleDao()) {

    fun execute(schedule: Schedule) {
        scheduleDao.delete(schedule)
    }

}
