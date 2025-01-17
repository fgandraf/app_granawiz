package core.entity.account

import core.entity.Group
import core.enums.AccountType
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("SAVINGS")
class SavingsAccount(
    name: String,
    description: String,
    position: Int,
    icon: String,
    balance: Double,
    group: Group,

    @Column(name = "open_balance")
    var openBalance: Double,

    ) : BankAccount(
    type = AccountType.SAVINGS,
    name = name,
    description = description,
    position = position,
    icon = icon,
    balance = balance,
    group = group
) {

    constructor(
        id: Long?,
        name: String,
        description: String = "",
        position: Int,
        icon: String,
        balance: Double,
        group: Group,
        openBalance: Double = 0.0,
    ) : this(
        name = name,
        description = description,
        position = position,
        icon = icon,
        balance = balance,
        group = group,
        openBalance = openBalance,
    ) {
        if (id != null) {
            this.id = id
        }
    }
}