package model.entity.account

import model.enums.AccountType
import model.entity.Group
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("CHECKING")
class CheckingAccount(
    name: String,
    description: String = "",
    position: Int,
    icon: String,
    balance: Double,
    group: Group,

    @Column(name = "open_balance")
    var openBalance: Double = 0.0,

    @Column(name = "overdraft_limit")
    var overdraftLimit: Double = 0.0,

    ) : BankAccount(type = AccountType.CHECKING, name = name, description = description, position = position, icon = icon, balance = balance, group = group) {

}