package application.group.usecases

import domain.entity.Group
import infrastructure.repository.GroupRepository

class DeleteGroupUseCase(private val groupRepository: GroupRepository = GroupRepository()) {


    fun execute(group: Group) {
        groupRepository.delete(group)
    }


}