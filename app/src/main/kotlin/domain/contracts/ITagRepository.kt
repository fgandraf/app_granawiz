package domain.contracts

import domain.entity.Tag

interface ITagRepository {

    fun getAll() : List<Tag>

    fun delete(tag: Tag)

    fun update(tag: Tag)

    fun insert(tag: Tag)
}