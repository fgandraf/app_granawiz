package core.contracts

import core.entity.Tag

interface ITagDao {

    fun getAll() : List<Tag>

    fun delete(tag: Tag)

    fun update(tag: Tag)

    fun insert(tag: Tag)
}