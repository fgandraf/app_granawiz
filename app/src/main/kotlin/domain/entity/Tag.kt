package domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "tbl_tags")
class Tag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id", columnDefinition = "INTEGER") val id: Long = 0,

    var name: String = "",

    @ManyToMany(mappedBy = "tags")
    val transactions: MutableSet<Transaction>? = mutableSetOf(),
) {
    constructor() : this(0, "")
}