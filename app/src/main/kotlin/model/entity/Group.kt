package model.entity

import model.entity.account.BankAccount
import jakarta.persistence.*

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