package domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "tbl_party_names")
class PartyName(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_name_id", columnDefinition = "INTEGER") val id: Long = 0,

    var name: String = "",

    @ManyToOne
    @JoinColumn(name = "party_id", referencedColumnName = "party_id") val party: Party,
) {
    constructor() : this(0, "", Party())
}