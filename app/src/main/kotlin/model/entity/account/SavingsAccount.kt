package model.entity.account

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import model.entity.Group
import model.enums.AccountType

@Entity
@DiscriminatorValue("SAVINGS")
class SavingsAccount(
    name: String,
    description: String,
    position: Int,
    icon: String,

    @Column(name = "open_balance")
    var openBalance: Double,

    balance: Double,
    group: Group
) : BankAccount(type = AccountType.SAVINGS, name = name, description = description, position = position, icon = icon, balance = balance, group = group){

}