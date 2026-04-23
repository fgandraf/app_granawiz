package application.tag.usecases

import domain.contracts.ITagRepository
import domain.entity.Tag
import infrastructure.repository.TagRepository

class DeleteTagUseCase(private val tagRepository: ITagRepository = TagRepository()) {

    fun execute(tag: Tag) {
        tagRepository.delete(tag)
    }

}