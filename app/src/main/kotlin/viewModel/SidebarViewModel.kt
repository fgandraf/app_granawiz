package viewModel

import core.entity.Group
import core.entity.account.BankAccount
import kotlinx.coroutines.flow.MutableStateFlow
import service.AccountService
import service.GroupService

class SidebarViewModel {

    private val accountService = AccountService()
    private val groupService = GroupService()

    var groups = MutableStateFlow(emptyList<Group>())
    fun getGroups() { groups.value = groupService.loadGroups() }


    var total = MutableStateFlow(0.0)
    fun getTotal() { total.value = groupService.fetchTotal() }

    fun getTotalFromGroup(group: Group): Double {return groupService.fetchTotalFromGroup(group) }


    fun renameGroup(group: Group, name: String) {
        groupService.renameGroup(group, name)
        getGroups()
    }

    fun moveGroupPosition(group: Group, direction: Int) {
        groupService.moveGroup(groups.value, group, direction)
        getGroups()
    }

    fun moveAccountPosition(account: BankAccount, direction: Int) {
        groupService.moveAccountPosition(groups.value, account, direction)
        getGroups()
    }

    fun reload(){
        getGroups()
        getTotal()
    }

    fun deleteGroup(group: Group) {
        groupService.deleteGroup(group)
        getGroups()
    }

    fun deleteAccount(account: BankAccount) {
        accountService.deleteAccount(account)
        reload()
    }

    fun addNewGroup(name: String) {
        groupService.addNewGroup(name)
        getGroups()
    }


    init {
        reload()
    }
}