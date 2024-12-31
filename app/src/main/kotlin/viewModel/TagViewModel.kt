package viewModel

import core.entity.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import service.TagService

class TagViewModel() {

    val service = TagService()

    var tags = MutableStateFlow(emptyList<Tag>())
    fun loadTags(){
        tags.value = service.loadTagsList()
    }

    var selectedTags = MutableStateFlow(emptyList<Tag>())

    fun deleteTag(tag: Tag) {
        service.deleteTag(tag)
        loadTags()
    }

    fun addTag(name: String){
        service.addTag(Tag(name = name))
        loadTags()
    }

    fun updateTag(tag: Tag, name: String){
        val updatedTag = Tag(id = tag.id, name = name)
        service.updateTag(updatedTag)
        loadTags()
    }

}