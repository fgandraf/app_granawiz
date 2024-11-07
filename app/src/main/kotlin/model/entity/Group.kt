package model.entity

import jakarta.persistence.*
import model.entity.account.BankAccount

@Entity
@Table(name = "TBL_GROUPS")
open class Group(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INTEGER")
    open val id: Long = 0,

    open val name: String,

    open var position: Int,

    @OneToMany(mappedBy = "group", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    open val accounts: MutableList<BankAccount> = mutableListOf()

) {}