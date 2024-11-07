package domain.contracts

import domain.entity.account.BankAccount

interface IAccountRepository {

    fun insert(account: BankAccount)

    fun update(account: BankAccount)

    fun delete(account: BankAccount)

    fun updateAccountPositions(accounts: List<BankAccount>)

    fun getAccountById(id: Long): BankAccount?
}