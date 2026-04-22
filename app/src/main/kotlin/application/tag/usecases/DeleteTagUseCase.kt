package application.tag.usecases

import domain.entity.Tag
import infrastructure.repository.TagRepository

class DeleteTagUseCase(private val tagRepository: TagRepository = TagRepository()) {

    fun execute(tag: Tag) {
        tagRepository.delete(tag)
    }

}