package model.entity

import jakarta.persistence.*

@Entity
@Table(name = "tbl_payers")
open class Payer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payer_id", columnDefinition = "INTEGER")
    open val id: Long = 0,

    open var name: String = "",

    @OneToMany(mappedBy = "payer", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    open val payersNames: MutableList<PayerName> = mutableListOf()
) {
    constructor() : this(0, "", mutableListOf())
}