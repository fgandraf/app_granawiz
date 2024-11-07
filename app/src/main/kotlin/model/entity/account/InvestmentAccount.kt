package model.entity.account

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import model.entity.Group
import model.enums.AccountType

@Entity
@DiscriminatorValue("INVESTMENT")
class InvestmentAccount(
    name: String,
    description: String,
    position: Int,
    icon: String,
    balance: Double,
    group: Group,
    var portfolio: String
) : BankAccount(type = AccountType.INVESTMENT, name = name, description = description, position = position, icon = icon, balance = balance, group = group) {
}