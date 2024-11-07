package domain.entity.account

import domain.entity.Group
import domain.enums.AccountType
import jakarta.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "account_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "tbl_bank_accounts")
class BankAccount(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", columnDefinition = "INTEGER") val id: Long = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", insertable = false, updatable = false) val type: AccountType,

    val name: String,
    val description: String = "",
    val icon: String,

    @Column(name = "icon_svg", columnDefinition = "TEXT") var iconSvg: String? = null,

    var balance: Double,
    var position: Int,

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "group_id") var group: Group,
) {
    constructor() : this(0L, AccountType.CHECKING, "", "", "_default.svg", null, 0.0, 0, Group())
}