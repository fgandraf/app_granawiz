package model.entity

import jakarta.persistence.*

@Entity
@Table(name = "tbl_receiver_names")
open class ReceiverName(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receiver_name_id", columnDefinition = "INTEGER")
    open val id: Long = 0,

    open var name: String = "",

    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "receiver_id")
    open val receiver: Receiver
) {
    constructor() : this(0, "", Receiver())
}