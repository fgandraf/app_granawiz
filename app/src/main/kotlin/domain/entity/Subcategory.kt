package domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "tbl_subcategories")
class Subcategory(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subcategory_id", columnDefinition = "INTEGER") var id: Long = 0,

    val name: String,

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id") val category: Category,
) {
    constructor() : this(0L, "", Category())
}