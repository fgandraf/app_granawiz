package model.entity

import jakarta.persistence.*

@Entity
@Table(name = "tbl_sub_categories")
open class SubCategory(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_category_id", columnDefinition = "INTEGER")
    open var id: Long = 0,

    open val name: String,

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    open val category: Category
)