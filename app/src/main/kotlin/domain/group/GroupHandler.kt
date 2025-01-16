package domain.group

import core.entity.Group
import domain.group.usecases.*

class GroupHandler{

    private val addNewGroup = AddNewGroupUseCase()
    private val deleteGroup = DeleteGroupUseCase()
    private val fetchGroupBalance = FetchGroupBalanceUseCase()
    private val fetchGroups = FetchGroupsUseCase()
    private val moveGroupPosition = MoveGroupPositionUseCase()
    private val renameGroup = RenameGroupUseCase()
    private val fetchTotalBalance = FetchTotalBalanceUseCase()


    fun addNewGroup(name: String) = addNewGroup.execute(name)
    fun deleteGroup(group: Group) = deleteGroup.execute(group)
    fun fetchGroupBalance(group: Group): Double = fetchGroupBalance.execute(group)
    fun fetchGroups() : List<Group> = fetchGroups.execute()
    fun moveGroupPosition(groups: List<Group>, group: Group, direction: Int) = moveGroupPosition.execute(groups, group, direction)
    fun renameGroup(group: Group, name: String) = renameGroup.execute(group, name)
    fun fetchTotalBalance(): Double = fetchTotalBalance.execute()

}