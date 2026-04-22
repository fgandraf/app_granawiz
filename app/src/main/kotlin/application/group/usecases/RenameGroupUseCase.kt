package application.group.usecases

import domain.entity.Group
import infrastructure.repository.GroupRepository

class RenameGroupUseCase(private val groupRepository: GroupRepository = GroupRepository()) {


    fun execute(group: Group, name: String) {
        val renamedGroup = Group(
            id = group.id,
            name = name,
            position = group.position,
            accounts = group.accounts
        )
        groupRepository.update(renamedGroup)
    }


}