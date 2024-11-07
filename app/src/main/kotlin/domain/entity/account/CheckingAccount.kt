package domain.entity.account

import domain.entity.Group
import domain.enums.AccountType
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("CHECKING")
class CheckingAccount(
    id: Long = 0,
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
    id = id,
    type = AccountType.CHECKING,
    name = name,
    description = description,
    icon = icon,
    iconSvg = iconSvg,
    balance = balance,
    position = position,
    group = group
)
