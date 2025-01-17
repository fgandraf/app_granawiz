package viewModel

import core.entity.Group
import core.entity.account.BankAccount
import domain.account.AccountHandler
import domain.group.GroupHandler
import kotlinx.coroutines.flow.MutableStateFlow

class SidebarViewModel(
    private val groupHandler: GroupHandler = GroupHandler(),
    private val accountHandler: AccountHandler = AccountHandler(),
) {

    var total = MutableStateFlow(0.0)
    fun getTotal() {
        total.value = groupHandler.fetchTotalBalance()
    }

    var groups = MutableStateFlow(emptyList<Group>())
    fun getGroups() {
        groups.value = groupHandler.fetchGroups()
    }

    fun fetchGroupBalance(group: Group): Double {
        return groupHandler.fetchGroupBalance(group)
    }

    fun renameGroup(group: Group, name: String) {
        groupHandler.renameGroup(group, name); getGroups()
    }

    fun moveGroupPosition(group: Group, direction: Int) {
        groupHandler.moveGroupPosition(groups.value, group, direction); getGroups()
    }

    fun moveAccountPosition(account: BankAccount, direction: Int) {
        accountHandler.moveAccountPosition(groups.value, account, direction); getGroups()
    }

    fun deleteGroup(group: Group) {
        groupHandler.deleteGroup(group); getGroups()
    }

    fun addNewGroup(name: String) {
        groupHandler.addNewGroup(name); getGroups()
    }

    fun deleteAccount(account: BankAccount) {
        accountHandler.deleteAccount(account); reload()
    }

    fun reload() {
        getGroups(); getTotal()
    }

    init {
        reload()
    }

}