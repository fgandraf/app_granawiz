package viewModel

import core.entity.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import domain.tag.TagHandler

class TagViewModel(private val tagHandler : TagHandler = TagHandler()) {

    var tags = MutableStateFlow(emptyList<Tag>())
    fun getTags(){ tags.value = tagHandler.fetchTags() }

    var selectedTags = MutableStateFlow(emptyList<Tag>())

    fun toggleTagSelection(tag: Tag) {
        selectedTags.value =
            if (selectedTags.value.any { it.id == tag.id }) selectedTags.value.filter { it.id != tag.id }
            else if(selectedTags.value.size < 3) selectedTags.value + tag else selectedTags.value
    }

    fun deleteTag(tag: Tag) {
        tagHandler.deleteTag(tag); getTags()
    }

    fun addTag(name: String){
        tagHandler.addTag(name)
        getTags()
    }

    fun updateTag(tag: Tag, name: String) {
        tagHandler.updateTag(tag, name)
        getTags()
    }

}