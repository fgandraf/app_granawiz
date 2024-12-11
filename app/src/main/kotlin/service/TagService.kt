package service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import core.entity.Tag
import infra.dao.TagDao

class TagService {

    private val dao: TagDao = TagDao()

    var tags by mutableStateOf(emptyList<Tag>())


    fun loadTags() {
        tags = dao.getAll()
    }

    fun deleteTag(tag: Tag) {
        dao.delete(tag)
        loadTags()
    }

    fun addTag(name: String) {
        val newTag = Tag(name = name)
        dao.insert(newTag)
        loadTags()
    }

    fun updateTag(tag: Tag, name: String) {
        val updatedTag = Tag(id = tag.id, name = name)
        dao.update(updatedTag)
        loadTags()
    }

}