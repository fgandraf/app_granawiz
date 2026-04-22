package application.tag.usecases

import domain.entity.Tag
import infrastructure.repository.TagRepository

class FetchTagsUseCase(private val tagRepository: TagRepository = TagRepository()) {

    fun execute(): List<Tag> {
        return tagRepository.getAll()
    }

}