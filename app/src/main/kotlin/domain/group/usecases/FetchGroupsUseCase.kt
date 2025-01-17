package domain.group.usecases

import core.entity.Group
import infra.dao.GroupDao

class FetchGroupsUseCase(private val groupDao: GroupDao = GroupDao()) {

    fun execute(): List<Group> {
        return groupDao.getAll()
    }


}