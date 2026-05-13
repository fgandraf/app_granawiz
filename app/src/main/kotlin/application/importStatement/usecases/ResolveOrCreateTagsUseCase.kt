package application.importStatement.usecases

import domain.contracts.ITagRepository
import domain.entity.Tag
import domain.structs.ParsedEntry
import infrastructure.repository.TagRepository

class ResolveOrCreateTagsUseCase(
    private val tagRepository: ITagRepository = TagRepository(),
) {

    private var cache: MutableList<Tag>? = null

    private fun getTags(): MutableList<Tag> =
        cache ?: tagRepository.getAll().toMutableList().also { cache = it }

    fun execute(entry: ParsedEntry): MutableList<Tag> {
        val text = entry.customTagsText?.trim().orEmpty()
        if (text.isBlank()) return mutableListOf()

        val names = text.split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .distinctBy { it.lowercase() }

        if (names.isEmpty()) return mutableListOf()

        val tags = getTags()
        return names.map { name ->
            tags.firstOrNull { it.name.equals(name, ignoreCase = true) }
                ?: Tag(name = name).also { newTag ->
                    tagRepository.insert(newTag)
                    tags.add(newTag)
                }
        }.toMutableList()
    }
}
