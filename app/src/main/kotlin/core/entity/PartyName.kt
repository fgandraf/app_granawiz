package core.entity

import jakarta.persistence.*

@Entity
@Table(name = "tbl_party_names")
open class PartyName(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_name_id", columnDefinition = "INTEGER")
    open val id: Long = 0,

    open var name: String = "",

    @ManyToOne
    @JoinColumn(name = "party_id", referencedColumnName = "party_id")
    open val party: Party
) {
    constructor() : this(0, "", Party())
}