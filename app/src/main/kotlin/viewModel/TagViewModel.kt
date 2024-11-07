package viewModel

import domain.entity.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import application.tag.TagHandler
import infrastructure.di.ApplicationContainer
import viewModel.shared.AppEvents
import viewModel.shared.UiEvent

class TagViewModel(private val tagHandler: TagHandler = ApplicationContainer.tagHandler) {

    private val _tags = MutableStateFlow(emptyList<Tag>())
    val tags: StateFlow<List<Tag>> = _tags.asStateFlow()
    fun getTags() {
        runCatching {
            _tags.value = tagHandler.fetchTags()
        }.onFailure { AppEvents.emit(UiEvent.Error(it.localizedMessage ?: "Erro desconhecido")) }
    }

    private val _selectedTags = MutableStateFlow(emptyList<Tag>())
    val selectedTags: StateFlow<List<Tag>> = _selectedTags.asStateFlow()
    fun setSelectedTags(tags: List<Tag>) { _selectedTags.value = tags }

    fun toggleTagSelection(tag: Tag) {
        _selectedTags.value =
            if (_selectedTags.value.any { it.id == tag.id }) _selectedTags.value.filter { it.id != tag.id }
            else if (_selectedTags.value.size < 3) _selectedTags.value + tag else _selectedTags.value
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
