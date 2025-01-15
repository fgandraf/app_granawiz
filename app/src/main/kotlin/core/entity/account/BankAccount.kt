package core.entity.account

import core.entity.Group
import core.enums.AccountType
import jakarta.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "tbl_bank_accounts")
open class BankAccount(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", columnDefinition = "INTEGER")
    open var id: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", insertable = false, updatable = false)
    open val type: AccountType,

    open val name: String,
    open val description: String = "",
    open val icon: String,
    open var balance: Double,
    open var position: Int,

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    open var group: Group
){
    constructor(): this(0L, AccountType.CHECKING, "", "", "_default.svg", 0.0, 0, Group())
}