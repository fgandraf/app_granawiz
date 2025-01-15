package service

import core.entity.Group
import core.entity.account.BankAccount
import infra.dao.AccountDao
import infra.dao.GroupDao

class GroupService {

    private val groupDao: GroupDao = GroupDao()
    private val accountDao: AccountDao = AccountDao()

    fun loadGroups(): List<Group> { return groupDao.getAll() }

    fun fetchTotalFromGroup(group: Group) : Double { return group.accounts.sumOf { it.balance } }

    fun fetchTotal(): Double { return groupDao.getAll().sumOf { group -> group.accounts.sumOf { it.balance } } }

    fun deleteGroup(group: Group) {
        groupDao.delete(group)
        loadGroups()
    }

    fun addNewGroup(name: String) {
        val groupSize = groupDao.getAll().count()
        val newGroup = Group(name = name, position = groupSize + 1)
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
        groupDao.update(renamedGroup)
    }

    fun moveGroup(groups: List<Group>, group: Group, direction: Int) {
        val groupIndex = groups.indexOf(group)
        val newIndex = groupIndex + direction

        if (groupIndex == -1 || newIndex !in groups.indices) return

        val updatedGroups = groups.toMutableList()
        updatedGroups[groupIndex] = updatedGroups[newIndex].also { updatedGroups[newIndex] = updatedGroups[groupIndex] }
        updatedGroups.forEachIndexed { index, grp -> grp.position = index + 1 }

        groupDao.updateGroupPositions(updatedGroups)
    }

    fun moveAccountPosition(groups: List<Group>, account: BankAccount, direction: Int) {
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
                accountDao.updateAccountPositions(accounts)
            }
        }
    }

}



