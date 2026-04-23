package application.tag.usecases

import domain.contracts.ITagRepository
import domain.entity.Tag
import infrastructure.repository.TagRepository

class FetchTagsUseCase(private val tagRepository: ITagRepository = TagRepository()) {

    fun execute(): List<Tag> {
        return tagRepository.getAll()
    }

}