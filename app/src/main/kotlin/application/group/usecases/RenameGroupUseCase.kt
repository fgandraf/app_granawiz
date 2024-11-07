package application.group.usecases

import domain.contracts.IGroupRepository
import domain.entity.Group
class RenameGroupUseCase(private val groupRepository: IGroupRepository) {


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