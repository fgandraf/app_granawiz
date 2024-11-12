package viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import model.dao.AccountDao
import model.dao.GroupDao
import model.entity.Group
import model.entity.account.BankAccount

class SidebarViewModel {

    var groups by mutableStateOf(emptyList<Group>()); private set
    var totalAccounts by mutableStateOf(0.0); private set

    init {
        loadGroup()
        fetchTotalAccounts()
    }

    fun loadGroup() {
        val dao = GroupDao()
        groups = dao.getAll()
    }

    fun fetchTotalAccounts() {
        totalAccounts = groups.sumOf { group -> group.accounts.sumOf { it.balance } }
    }

    fun fetchTotalGroup(group: Group) : Double {
        return group.accounts.sumOf { it.balance }
    }

    fun deleteAccount(account: BankAccount) {
        val dao = AccountDao()
        dao.delete(account)
        loadGroup()
    }

    fun deleteGroup(group: Group) {
        val dao = GroupDao()
        dao.delete(group)
        loadGroup()
    }

    fun moveAccount(group: Group, account: BankAccount, direction: Int) {
        val groupIndex = groups.indexOf(group)
        if (groupIndex != -1) {
            val accountIndex = groups[groupIndex].accounts.indexOf(account)
            val newIndex = accountIndex + direction

            if (newIndex in 0 until groups[groupIndex].accounts.size) {
                val accounts = groups[groupIndex].accounts.toMutableList()
                accounts[accountIndex] = accounts[newIndex]
                accounts[newIndex] = account

                accounts.forEachIndexed { index, acc -> acc.position = index + 1 }

                groups = groups.toMutableList().apply {
                    this[groupIndex] = Group(
                        id = group.id,
                        name = group.name,
                        accounts = accounts,
                        position = group.position
                    )
                }

                val dao = AccountDao()
                dao.updateAccountPositions(accounts)
                loadGroup()
            }
        }
    }

    fun moveGroup(group: Group, direction: Int) {
        val groupIndex = groups.indexOf(group)
        val newIndex = groupIndex + direction

        if (groupIndex == -1 || newIndex !in groups.indices) return

        val updatedGroups = groups.toMutableList()
        updatedGroups[groupIndex] = updatedGroups[newIndex].also { updatedGroups[newIndex] = updatedGroups[groupIndex] }
        updatedGroups.forEachIndexed { index, grp -> grp.position = index + 1 }

        groups = updatedGroups

        val dao = GroupDao()
        dao.updateGroupPositions(updatedGroups)

        loadGroup()
    }

    fun renameGroup(group: Group, name: String) {
        val renamedGroup = Group(
            id = group.id,
            name = name,
            position = group.position,
            accounts = group.accounts
        )
        val dao = GroupDao()
        dao.update(renamedGroup)
        groups = groups.map {
            if (it.id == group.id)
                Group(it.id, name, it.position, it.accounts)
            else it
        }
    }

    fun addNewGroup(name: String) {
        val newGroup = Group(name = name, position = groups.size + 1)
        val dao = GroupDao()
        dao.insert(newGroup)
        loadGroup()
    }

}