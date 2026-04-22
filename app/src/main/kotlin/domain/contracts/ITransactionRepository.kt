package domain.contracts

import domain.entity.Transaction
import domain.entity.account.BankAccount

interface ITransactionRepository {

    fun getAll() : List<Transaction>

    fun getAllByAccount(account: BankAccount) : List<Transaction>

}