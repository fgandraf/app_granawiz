package application.group.usecases

import domain.entity.Group
import infrastructure.repository.GroupRepository

class MoveGroupPositionUseCase(private val groupRepository: GroupRepository = GroupRepository()) {


    fun execute(groups: List<Group>, group: Group, direction: Int) {
        val groupIndex = groups.indexOf(group)
        val newIndex = groupIndex + direction

        if (groupIndex == -1 || newIndex !in groups.indices) return

        val updatedGroups = groups.toMutableList()
        updatedGroups[groupIndex] = updatedGroups[newIndex].also { updatedGroups[newIndex] = updatedGroups[groupIndex] }
        updatedGroups.forEachIndexed { index, grp -> grp.position = index + 1 }

        groupRepository.updateGroups(updatedGroups)
    }


}