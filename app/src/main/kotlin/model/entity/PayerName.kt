package model.entity

import jakarta.persistence.*

@Entity
@Table(name = "tbl_payer_names")
open class PayerName(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payer_name_id", columnDefinition = "INTEGER")
    open val id: Long = 0,

    open var name: String = "",

    @ManyToOne
    @JoinColumn(name = "payer_id", referencedColumnName = "payer_id")
    open val payer: Payer
) {
    constructor() : this(0, "", Payer())
}