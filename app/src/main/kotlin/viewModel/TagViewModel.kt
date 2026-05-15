package viewModel

import domain.entity.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import application.tag.TagHandler
import infrastructure.di.ApplicationContainer
import viewModel.shared.AppEvents
import viewModel.shared.UiEvent

class TagViewModel(private val tagHandler: TagHandler = ApplicationContainer.tagHandler) {

    var tags = MutableStateFlow(emptyList<Tag>())
    fun getTags() {
        runCatching {
            tags.value = tagHandler.fetchTags()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    var selectedTags = MutableStateFlow(emptyList<Tag>())

    fun toggleTagSelection(tag: Tag) {
        selectedTags.value =
            if (selectedTags.value.any { it.id == tag.id }) selectedTags.value.filter { it.id != tag.id }
            else if (selectedTags.value.size < 3) selectedTags.value + tag else selectedTags.value
    }

    fun deleteTag(tag: Tag) {
        runCatching {
            tagHandler.deleteTag(tag); getTags()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun addTag(name: String) {
        runCatching {
            tagHandler.addTag(name)
            getTags()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    fun updateTag(tag: Tag, name: String) {
        runCatching {
            tagHandler.updateTag(tag, name)
            getTags()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }
}
