package domain.entity.account

import domain.entity.Group
import domain.enums.AccountType
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("SAVINGS")
class SavingsAccount(
    id: Long = 0,
    name: String,
    description: String,
    position: Int,
    icon: String,
    iconSvg: String? = null,
    balance: Double,
    group: Group,
    @Column(name = "open_balance")
    var openBalance: Double,
) : BankAccount(
    id = id,
    type = AccountType.SAVINGS,
    name = name,
    description = description,
    icon = icon,
    iconSvg = iconSvg,
    balance = balance,
    position = position,
    group = group
)
