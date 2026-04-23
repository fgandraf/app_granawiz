package application.tag.usecases

import domain.contracts.ITagRepository
import domain.entity.Tag
import infrastructure.repository.TagRepository

class AddTagUseCase(private val tagRepository: ITagRepository = TagRepository()) {

    fun execute(name: String) {
        val tag = Tag(name = name)
        tagRepository.insert(tag)
    }

}