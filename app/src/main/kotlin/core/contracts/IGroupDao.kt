package core.contracts

import core.entity.Group

interface IGroupDao {

    fun getAll() : List<Group>

    fun delete(group: Group)

    fun updateGroups(accounts: List<Group>)

    fun update(group: Group)

    fun insert(group: Group)
}