package application.tag

import domain.contracts.ITagRepository
import domain.entity.Tag
import infrastructure.repository.TagRepository

class TagHandler {

    private val tagRepository: ITagRepository = TagRepository()

    fun addTag(name: String) = tagRepository.insert(Tag(name = name))
    fun deleteTag(tag: Tag) = tagRepository.delete(tag)
    fun fetchTags(): List<Tag> = tagRepository.getAll()
    fun updateTag(tag: Tag, name: String) = tagRepository.update(Tag(id = tag.id, name = name))
}
