package application.tag

import domain.entity.Tag
import application.tag.usecases.AddTagUseCase
import application.tag.usecases.DeleteTagUseCase
import application.tag.usecases.FetchTagsUseCase
import application.tag.usecases.UpdateTagUseCase

class TagHandler {

    private val addTagUseCase = AddTagUseCase()
    private val deleteTagUseCase = DeleteTagUseCase()
    private val fetchTagsUseCase = FetchTagsUseCase()
    private val updateTagUseCase = UpdateTagUseCase()


    fun addTag(name: String) = addTagUseCase.execute(name)
    fun deleteTag(tag: Tag) = deleteTagUseCase.execute(tag)
    fun fetchTags(): List<Tag> = fetchTagsUseCase.execute()
    fun updateTag(tag: Tag, name: String) = updateTagUseCase.execute(tag, name)

}