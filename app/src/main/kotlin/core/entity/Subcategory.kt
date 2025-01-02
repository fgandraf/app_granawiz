package core.entity

import jakarta.persistence.*

@Entity
@Table(name = "tbl_subcategories")
open class Subcategory(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subcategory_id", columnDefinition = "INTEGER")
    open var id: Long = 0,

    open val name: String,

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    open val category: Category
){
    constructor() : this(0L, "", Category())
}