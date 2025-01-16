package domain.tag.usecases

import core.entity.Tag
import infra.dao.TagDao

class UpdateTagUseCase(private val tagDao: TagDao = TagDao()) {

    fun execute(tag: Tag, name: String) {
        val updatedTag = Tag(id = tag.id, name = name)
        tagDao.update(updatedTag)
    }

}