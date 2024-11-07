package domain.entity.account

import domain.entity.Group
import domain.enums.AccountType
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("CREDIT_CARD")
class CreditCardAccount(
    id: Long = 0,
    name: String,
    description: String,
    position: Int,
    icon: String,
    iconSvg: String? = null,
    balance: Double,
    group: Group,
    @Column(name = "credit_limit")
    var creditLimit: Double,
    @Column(name = "closing_day")
    val closingDay: Int,
    @Column(name = "due_day")
    val dueDay: Int,
) : BankAccount(
    id = id,
    type = AccountType.CREDIT_CARD,
    name = name,
    description = description,
    icon = icon,
    iconSvg = iconSvg,
    balance = balance,
    position = position,
    group = group
)
