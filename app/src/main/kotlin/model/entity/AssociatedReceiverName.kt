package model.entity

import jakarta.persistence.*

@Entity
@Table(name = "tbl_associated_receivers_names")
open class AssociatedReceiverName(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "associated_receivers_names_id", columnDefinition = "INTEGER")
    open val id: Long = 0,

    open var name: String = "",

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "receiver_id")
    open val receiver: Receiver
) {
    constructor() : this(0, "", Receiver())
}