package domain.tag.usecases

import core.entity.Tag
import infra.dao.TagDao

class FetchTagsUseCase(private val tagDao: TagDao = TagDao()) {

    fun execute(): List<Tag>{
        return tagDao.getAll()
    }

}