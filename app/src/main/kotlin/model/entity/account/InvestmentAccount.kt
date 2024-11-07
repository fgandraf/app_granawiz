package model.entity.account

import model.enums.AccountType
import model.entity.Group
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

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