package application.group.usecases

import domain.contracts.IGroupRepository
import domain.entity.Group
import infrastructure.repository.GroupRepository

class FetchGroupsUseCase(private val groupRepository: IGroupRepository = GroupRepository()) {

    fun execute(): List<Group> {
        return groupRepository.getAll()
    }


}