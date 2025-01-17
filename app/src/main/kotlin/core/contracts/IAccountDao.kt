package core.contracts

import core.entity.account.BankAccount

interface IAccountDao {

    fun insert(account: BankAccount)

    fun update(account: BankAccount)

    fun delete(account: BankAccount)

    fun updateAccountPositions(accounts: List<BankAccount>)

    fun getAccountById(id: Long): BankAccount?
}