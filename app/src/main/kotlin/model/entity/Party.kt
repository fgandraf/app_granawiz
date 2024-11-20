package model.entity

import jakarta.persistence.*
import model.enums.PartyType

@Entity
@Table(name = "tbl_parties")
open class Party(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id", columnDefinition = "INTEGER")
    open val id: Long = 0,

    open var name: String = "",

    @Enumerated(EnumType.STRING)
    @Column(name = "type", insertable = true, updatable = true)
    open val type: PartyType,

    @OneToMany(mappedBy = "party", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    open val partiesNames: MutableList<PartyName> = mutableListOf()
) {
    constructor() : this(0, "", PartyType.PAYER, mutableListOf())
}