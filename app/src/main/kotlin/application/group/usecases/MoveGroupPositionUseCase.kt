package application.group.usecases

import domain.contracts.IGroupRepository
import domain.entity.Group
class MoveGroupPositionUseCase(private val groupRepository: IGroupRepository) {


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