package domain.group

import core.entity.Group
import domain.group.usecases.*

class GroupHandler(
    private val addNewGroup: AddNewGroupUseCase = AddNewGroupUseCase(),
    private val deleteGroup: DeleteGroupUseCase = DeleteGroupUseCase(),
    private val fetchGroupBalance: FetchGroupBalanceUseCase = FetchGroupBalanceUseCase(),
    private val fetchGroups: FetchGroupsUseCase = FetchGroupsUseCase(),
    private val moveGroupPosition: MoveGroupPositionUseCase = MoveGroupPositionUseCase(),
    private val renameGroup: RenameGroupUseCase = RenameGroupUseCase(),
    private val fetchTotalBalance: FetchTotalBalanceUseCase = FetchTotalBalanceUseCase(),
) {

    fun addNewGroup(name: String) = addNewGroup.execute(name)
    fun deleteGroup(group: Group) = deleteGroup.execute(group)
    fun fetchGroupBalance(group: Group): Double = fetchGroupBalance.execute(group)
    fun fetchGroups() : List<Group> = fetchGroups.execute()
    fun moveGroupPosition(groups: List<Group>, group: Group, direction: Int) = moveGroupPosition.execute(groups, group, direction)
    fun renameGroup(group: Group, name: String) = renameGroup.execute(group, name)
    fun fetchTotalBalance(): Double = fetchTotalBalance.execute()

}