package core.entity.account

import core.entity.Group
import core.enums.AccountType
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("CREDIT_CARD")
class CreditCardAccount(
    name: String,
    description: String,
    position: Int,
    icon: String,
    balance: Double,
    group: Group,

    @Column(name = "credit_limit")
    var creditLimit: Double,

    @Column(name = "closing_day")
    val closingDay: Int,

    @Column(name = "due_day")
    val dueDay: Int,
) : BankAccount(
    type = AccountType.CREDIT_CARD,
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
        creditLimit: Double = 0.0,
        closingDay: Int = 0,
        dueDay: Int = 0,
    ) : this(
        name = name,
        description = description,
        position = position,
        icon = icon,
        balance = balance,
        group = group,
        creditLimit = creditLimit,
        closingDay = closingDay,
        dueDay = dueDay
    ) {
        if (id != null) {
            this.id = id
        }
    }
}