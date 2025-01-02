package core.contracts

import core.entity.Transaction
import core.entity.account.BankAccount

interface ITransactionDao {

    fun getAll() : List<Transaction>

    fun getAllByAccount(account: BankAccount) : List<Transaction>

}