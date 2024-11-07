package domain.entity

import domain.enums.PartyType
import jakarta.persistence.*

@Entity
@Table(name = "tbl_parties")
class Party(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_id", columnDefinition = "INTEGER") val id: Long = 0,

    var name: String = "",

    @Enumerated(EnumType.STRING)
    @Column(name = "type", insertable = true, updatable = true) val type: PartyType,

    @OneToMany(mappedBy = "party", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true) val partiesNames: MutableList<PartyName> = mutableListOf(),
) {
    constructor() : this(0, "", PartyType.PAYER, mutableListOf())
}