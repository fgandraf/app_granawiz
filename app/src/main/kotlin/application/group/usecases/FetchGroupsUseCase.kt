package application.group.usecases

import domain.entity.Group
import infrastructure.repository.GroupRepository

class FetchGroupsUseCase(private val groupRepository: GroupRepository = GroupRepository()) {

    fun execute(): List<Group> {
        return groupRepository.getAll()
    }


}