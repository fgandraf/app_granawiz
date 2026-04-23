package application.group.usecases

import domain.contracts.IGroupRepository
import domain.entity.Group
import infrastructure.repository.GroupRepository

class DeleteGroupUseCase(private val groupRepository: IGroupRepository = GroupRepository()) {


    fun execute(group: Group) {
        groupRepository.delete(group)
    }


}