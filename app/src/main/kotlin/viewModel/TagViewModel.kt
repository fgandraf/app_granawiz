package viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import model.dao.TagDao
import model.entity.Tag

class TagViewModel {

    var tags by mutableStateOf(emptyList<Tag>()); private set
    private val tagDao = TagDao()

    init {
        loadTags()
    }

    private fun loadTags() {
        tags = tagDao.getAll()
    }

    fun deleteTag(tag: Tag) {
        tagDao.delete(tag)
        loadTags()
    }

    fun addTag(name: String) {
        val newTag = Tag(name = name)
        tagDao.insert(newTag)
        loadTags()
    }

    fun updateTag(tag: Tag, name: String) {
        val updatedTag = Tag(id = tag.id, name = name)
        tagDao.update(updatedTag)
        loadTags()
    }

}