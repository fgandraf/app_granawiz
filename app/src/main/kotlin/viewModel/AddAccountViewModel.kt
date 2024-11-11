package viewModel

import model.dao.AccountDao
import model.entity.account.BankAccount

class AddAccountViewModel {

    fun addAccount(account: BankAccount){
        val dao = AccountDao()
        dao.insert(account)
    }
}