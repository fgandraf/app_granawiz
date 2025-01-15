package domain.tag

import core.entity.Tag
import domain.tag.usecases.AddTagUseCase
import domain.tag.usecases.DeleteTagUseCase
import domain.tag.usecases.FetchTagsUseCase
import domain.tag.usecases.UpdateTagUseCase

class TagHandler(
    private val addTagUseCase: AddTagUseCase = AddTagUseCase(),
    private val deleteTagUseCase: DeleteTagUseCase = DeleteTagUseCase(),
    private val fetchTagsUseCase: FetchTagsUseCase = FetchTagsUseCase(),
    private val updateTagUseCase: UpdateTagUseCase = UpdateTagUseCase()
    ) {

    fun addTag(tag: Tag) = addTagUseCase.execute(tag)
    fun deleteTag(tag: Tag) = deleteTagUseCase.execute(tag)
    fun fetchTags(): List<Tag> = fetchTagsUseCase.execute()
    fun updateTag(tag: Tag) = updateTagUseCase.execute(tag)

}