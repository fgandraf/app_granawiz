package model.entity

import jakarta.persistence.*

@Entity
@Table(name = "tbl_tags")
open class Tag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id", columnDefinition = "INTEGER")
    open val id: Long = 0,

    open var name: String = "",

    @ManyToMany(mappedBy = "tags")
    val transactions: MutableSet<Transaction>? = mutableSetOf()
) {
    constructor() : this(0, "")
}