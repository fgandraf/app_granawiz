package application.tag.usecases

import domain.contracts.ITagRepository
import domain.entity.Tag
import infrastructure.repository.TagRepository

class UpdateTagUseCase(private val tagRepository: ITagRepository = TagRepository()) {

    fun execute(tag: Tag, name: String) {
        val updatedTag = Tag(id = tag.id, name = name)
        tagRepository.update(updatedTag)
    }

}