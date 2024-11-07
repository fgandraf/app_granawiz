package model.entity.account

import model.enums.AccountType
import model.entity.Group
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

    @Column(name = "closing_date")
    val closingDate: String,

    @Column(name = "due_date")
    val dueDate: String
) : BankAccount(type = AccountType.CREDIT_CARD, name = name, description = description, position = position, icon = icon, balance = balance, group = group) {
}