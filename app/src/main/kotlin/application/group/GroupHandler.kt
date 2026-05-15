package application.group

import domain.contracts.IGroupRepository
import domain.entity.Group
import application.group.usecases.AddNewGroupUseCase
import application.group.usecases.MoveGroupPositionUseCase
import application.group.usecases.RenameGroupUseCase
import infrastructure.repository.GroupRepository

class GroupHandler {

    private val groupRepository: IGroupRepository = GroupRepository()
    private val addNewGroup = AddNewGroupUseCase()
    private val moveGroupPosition = MoveGroupPositionUseCase()
    private val renameGroup = RenameGroupUseCase()

    fun addNewGroup(name: String) = addNewGroup.execute(name)
    fun deleteGroup(group: Group) = groupRepository.delete(group)
    fun fetchGroupBalance(group: Group): Double = group.accounts.sumOf { it.balance }
    fun fetchGroups(): List<Group> = groupRepository.getAll()
    fun moveGroupPosition(groups: List<Group>, group: Group, direction: Int) =
        moveGroupPosition.execute(groups, group, direction)
    fun renameGroup(group: Group, name: String) = renameGroup.execute(group, name)
    fun fetchTotalBalance(): Double = groupRepository.getAll().sumOf { g -> g.accounts.sumOf { it.balance } }
}
