package application.tag.usecases

import domain.entity.Tag
import infrastructure.repository.TagRepository

class AddTagUseCase(private val tagRepository: TagRepository = TagRepository()) {

    fun execute(name: String) {
        val tag = Tag(name = name)
        tagRepository.insert(tag)
    }

}