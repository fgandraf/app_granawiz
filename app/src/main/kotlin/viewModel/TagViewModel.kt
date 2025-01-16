package viewModel

import core.entity.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import domain.tag.TagHandler

class TagViewModel(private val handler : TagHandler = TagHandler()) {

    var tags = MutableStateFlow(emptyList<Tag>())
    fun loadTags(){
        tags.value = handler.fetchTags()
    }

    var selectedTags = MutableStateFlow(emptyList<Tag>())

    fun toggleTagSelection(tag: Tag) {
        selectedTags.value =
            if (selectedTags.value.any { it.id == tag.id }) selectedTags.value.filter { it.id != tag.id }
            else if(selectedTags.value.size < 3) selectedTags.value + tag else selectedTags.value
    }

    fun deleteTag(tag: Tag) {
        handler.deleteTag(tag)
        loadTags() // TODO: Remover da lista em memória
    }

    fun addTag(name: String){
        handler.addTag(Tag(name = name)) // TODO: Mover regras para os use cases
        loadTags() // TODO: Adicioanr à lista em memória
    }

    fun updateTag(tag: Tag, name: String){
        val updatedTag = Tag(id = tag.id, name = name)
        handler.updateTag(updatedTag)
        loadTags() // TODO: Atualizar da lista em memória
    }

}