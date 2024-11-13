package model.entity.account

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import model.entity.Group
import model.enums.AccountType

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
    val dueDay: Int
) : BankAccount(type = AccountType.CREDIT_CARD, name = name, description = description, position = position, icon = icon, balance = balance, group = group) {

    constructor(
        id: Long,
        name: String,
        description: String = "",
        position: Int,
        icon: String,
        balance: Double,
        group: Group,
        creditLimit: Double = 0.0,
        closingDay: Int = 0,
        dueDay: Int = 0
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
        this.id = id
    }
}