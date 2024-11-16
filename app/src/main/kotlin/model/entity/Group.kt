package model.entity

import jakarta.persistence.*
import model.entity.account.BankAccount

@Entity
@Table(name = "tbl_groups")
open class Group(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", columnDefinition = "INTEGER")
    open val id: Long = 0,

    open var name: String = "",

    open var position: Int = 0,

    @OneToMany(mappedBy = "group", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    open val accounts: MutableList<BankAccount> = mutableListOf()

) {
    constructor() : this(0, "", 0, mutableListOf())
}