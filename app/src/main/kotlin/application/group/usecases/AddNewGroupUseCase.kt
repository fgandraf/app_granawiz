package application.group.usecases

import domain.contracts.IGroupRepository
import domain.entity.Group
class AddNewGroupUseCase(private val groupRepository: IGroupRepository) {


    fun execute(name: String) {
        val groupSize = groupRepository.getAll().count()
        val newGroup = Group(name = name, position = groupSize + 1)
        groupRepository.insert(newGroup)
    }


}