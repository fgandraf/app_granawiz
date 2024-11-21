package model.entity

import jakarta.persistence.*
import model.enums.CategoryType

@Entity
@Table(name = "tbl_categories")
open class Category(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", columnDefinition = "INTEGER")
    open val id: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", insertable = true, updatable = true)
    open val type: CategoryType,

    open var name: String = "",

    open var icon: String = "",


    @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    open val subcategories: MutableList<Subcategory> = mutableListOf()

) {
    constructor() : this(0, CategoryType.INCOME, "", "", mutableListOf())
}