package domain.entity

import domain.enums.CategoryType
import jakarta.persistence.*

@Entity
@Table(name = "tbl_categories")
class Category(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", columnDefinition = "INTEGER") var id: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", insertable = true, updatable = true) val type: CategoryType,

    var name: String = "",

    var icon: String = "",


    @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true) val subcategories: MutableList<Subcategory> = mutableListOf(),

    ) {
    constructor() : this(0, CategoryType.INCOME, "", "", mutableListOf())
}