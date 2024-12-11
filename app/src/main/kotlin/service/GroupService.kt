package service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import core.entity.Group
import core.entity.account.BankAccount
import infra.dao.AccountDao
import infra.dao.GroupDao

class GroupService {

    private val groupDao: GroupDao = GroupDao()
    private val accountDao: AccountDao = AccountDao()

    var groups by mutableStateOf(emptyList<Group>())
    var totalAccounts by mutableStateOf(0.0); private set

    fun loadGroups() {
        groups = groupDao.getAll()
    }

    fun fetchTotalAccounts() {
        totalAccounts = groups.sumOf { group -> group.accounts.sumOf { it.balance } }
    }

    fun fetchTotalGroup(group: Group) : Double {
        return group.accounts.sumOf { it.balance }
    }

    fun deleteGroup(group: Group) {
        groupDao.delete(group)
        loadGroups()
    }

    fun addNewGroup(name: String) {
        val newGroup = Group(name = name, position = groups.size + 1)
        groupDao.insert(newGroup)
        loadGroups()
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

        loadGroups()
    }


    fun moveAccountPosition(account: BankAccount, direction: Int) {
        val group = account.group
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

                accountDao.updateAccountPositions(accounts)
                loadGroups()
            }
        }
    }



}