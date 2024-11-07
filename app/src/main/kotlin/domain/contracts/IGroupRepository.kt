package domain.contracts

import domain.entity.Group

interface IGroupRepository {

    fun getAll() : List<Group>

    fun delete(group: Group)

    fun updateGroups(accounts: List<Group>)

    fun update(group: Group)

    fun insert(group: Group)
}