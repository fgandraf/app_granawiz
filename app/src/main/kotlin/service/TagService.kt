package service

import core.entity.Tag
import infra.dao.TagDao

class TagService {

    private val dao: TagDao = TagDao()

    fun loadTagsList(): List<Tag> { return dao.getAll() }

    fun deleteTag(tag: Tag) { dao.delete(tag) }

    fun addTag(tag: Tag) { dao.insert(tag) }

    fun updateTag(tag: Tag) { dao.update(tag) }

}