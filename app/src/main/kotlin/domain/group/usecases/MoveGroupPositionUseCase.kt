package domain.group.usecases

import core.entity.Group
import infra.dao.GroupDao

class MoveGroupPositionUseCase(private val groupDao: GroupDao = GroupDao()) {


    fun execute(groups: List<Group>, group: Group, direction: Int) {
        val groupIndex = groups.indexOf(group)
        val newIndex = groupIndex + direction

        if (groupIndex == -1 || newIndex !in groups.indices) return

        val updatedGroups = groups.toMutableList()
        updatedGroups[groupIndex] = updatedGroups[newIndex].also { updatedGroups[newIndex] = updatedGroups[groupIndex] }
        updatedGroups.forEachIndexed { index, grp -> grp.position = index + 1 }

        groupDao.updateGroups(updatedGroups)
    }


}