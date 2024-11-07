package model.entity.account

import model.enums.AccountType
import model.entity.Group
import jakarta.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "TBL_BANK_ACCOUNTS")
open class BankAccount(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INTEGER")
    open val id: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", insertable = false, updatable = false)
    open val type: AccountType,

    open val name: String,
    open val description: String = "",
    open val icon: String,
    open val balance: Double,
    open var position: Int,

    @ManyToOne
    open val group: Group
)