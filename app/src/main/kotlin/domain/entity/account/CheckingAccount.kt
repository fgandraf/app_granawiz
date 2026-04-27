package domain.entity.account

import domain.entity.Group
import domain.enums.AccountType
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("CHECKING")
class CheckingAccount(
    name: String,
    description: String = "",
    position: Int,
    icon: String,
    iconSvg: String? = null,
    balance: Double,
    group: Group,
    @Column(name = "open_balance")
    var openBalance: Double = 0.0,

    @Column(name = "overdraft_limit")
    var overdraftLimit: Double = 0.0,
) : BankAccount(
    type = AccountType.CHECKING,
    name = name,
    description = description,
    icon = icon,
    iconSvg = iconSvg,
    balance = balance,
    position = position,
    group = group
) {

    constructor(
        id: Long?,
        name: String,
        description: String = "",
        position: Int,
        icon: String,
        iconSvg: String? = null,
        balance: Double,
        group: Group,
        openBalance: Double = 0.0,
        overdraftLimit: Double = 0.0,
    ) : this(
        name = name,
        description = description,
        position = position,
        icon = icon,
        iconSvg = iconSvg,
        balance = balance,
        group = group,
        openBalance = openBalance,
        overdraftLimit = overdraftLimit
    ) {
        if (id != null) {
            this.id = id
        }
    }
}