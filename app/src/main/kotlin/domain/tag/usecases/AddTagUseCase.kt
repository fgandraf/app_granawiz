package domain.tag.usecases

import core.entity.Tag
import infra.dao.TagDao

class AddTagUseCase(private val tagDao: TagDao = TagDao()) {

    fun execute(tag: Tag) {
        tagDao.insert(tag)
    }

}