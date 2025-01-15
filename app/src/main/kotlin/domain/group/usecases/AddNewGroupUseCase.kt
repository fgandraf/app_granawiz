package domain.group.usecases

import core.entity.Group
import infra.dao.GroupDao

class AddNewGroupUseCase(private val groupDao: GroupDao = GroupDao()) {


    fun execute(name: String) {
        val groupSize = groupDao.getAll().count()
        val newGroup = Group(name = name, position = groupSize + 1)
        groupDao.insert(newGroup)
    }


}