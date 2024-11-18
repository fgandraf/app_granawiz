package model.entity

import jakarta.persistence.*

@Entity
@Table(name = "tbl_receivers")
open class Receiver(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receiver_id", columnDefinition = "INTEGER")
    open val id: Long = 0,

    open var name: String = "",

    @OneToMany(mappedBy = "receiver", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    open val receiverNames: MutableList<ReceiverName> = mutableListOf()
) {
    constructor() : this(0, "", mutableListOf())
}