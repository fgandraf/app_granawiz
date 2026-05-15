package domain.entity

import domain.entity.account.BankAccount
import jakarta.persistence.*

@Entity
@Table(name = "tbl_groups")
class Group(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", columnDefinition = "INTEGER") val id: Long = 0,

    var name: String = "",

    var position: Int = 0,

    @OneToMany(mappedBy = "group", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true) val accounts: MutableList<BankAccount> = mutableListOf(),

    ) {
    constructor() : this(0, "", 0, mutableListOf())
}