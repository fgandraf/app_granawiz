package application.group.usecases

import domain.entity.Group
import infrastructure.repository.GroupRepository

class AddNewGroupUseCase(private val groupRepository: GroupRepository = GroupRepository()) {


    fun execute(name: String) {
        val groupSize = groupRepository.getAll().count()
        val newGroup = Group(name = name, position = groupSize + 1)
        groupRepository.insert(newGroup)
    }


}