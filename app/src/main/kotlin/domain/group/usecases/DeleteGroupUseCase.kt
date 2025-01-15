package domain.group.usecases

import core.entity.Group
import infra.dao.GroupDao

class DeleteGroupUseCase(private val groupDao: GroupDao = GroupDao()) {


    fun execute(group: Group) {
        groupDao.delete(group)
    }


}