package domain.group.usecases

import core.entity.Group
import infra.dao.GroupDao

class RenameGroupUseCase(private val groupDao: GroupDao = GroupDao()) {


    fun execute(group: Group, name: String) {
        val renamedGroup = Group(
            id = group.id,
            name = name,
            position = group.position,
            accounts = group.accounts
        )
        groupDao.update(renamedGroup)
    }


}